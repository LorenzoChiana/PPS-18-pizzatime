package gamemanager

import utilities.{Action, BonusSound, Direction, FailureSound, InjurySound, Intent, LevelMusic, LevelUpSound, Movement, SettingPreferences, Shoot, ShootSound}
import handlers.PreferencesHandler._
import gameview.scene.{Scene, SceneType}
import utilities.MessageTypes._
import SceneType._
import gameview.Window

import scala.collection.immutable.Queue
import utilities.Difficulty._
import Runtime.getRuntime
import java.io.PrintWriter
import java.util.concurrent.Executors.newFixedThreadPool

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.fromExecutorService
import gamelogic.{GameState, MapGenerator}
import gamelogic.GameState.{arena, playerRankings, worldRecord}
import gamemanager.ImageLoader.loadImage
import gamemanager.SoundLoader.{play, stopSound}
import gameview.fx.FXWindow
import gameview.fx.FXWindow.addObserver
import javafx.stage.Stage
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import scala.io.Source
import scala.util.{Failure, Success, Using}

object GameManager extends ViewObserver with GameLogicObserver {

  val NumThreads: Int = getRuntime.availableProcessors() + 1
  implicit val ThreadPool: ExecutionContext = fromExecutorService(newFixedThreadPool(NumThreads))
  val TimeSliceMillis: Int = 50
  var view: Option[Scene] = None
  def view_(view: Scene): Unit = this.view = Some(view)

  var numCycle: Int = 0
  lazy val windowManager: Window = view.get.windowManager

  var endGame: Boolean = false
  /** [[Queue]] for movements notified but not yet processed. */
  var playerMoves: Queue[Option[Direction]] = Queue[Option[Direction]]()
  /** [[Queue]] for shoots notified but not yet processed. */
  var playerShoots: Queue[Option[Direction]] = Queue[Option[Direction]]()

  loadPlayerRankings()

  /** Call by main for initialize game */
  def initializeGame(primaryStage: Stage) : Future[Unit] = Future{
    val view: Window = FXWindow(primaryStage)

    addObserver(this)

    loadImage().onComplete({
      case Success(_) =>  view.scene_(new Intent(MainScene)); view.showView()
      case Failure(t) => println(t.printStackTrace())
    })
  }

  /** Notifies that the game has started. */
  def notifyStartGame(): Unit = {
    endGame = false
    GameState.addObserver(this)
    GameState.startGame(playerName, MapGenerator(difficulty))
    ThreadPool.execute(new GameLoop())
    loadWorldRecord()
  }

  /** Notifies that the game has ended */
  def notifyEndGame(): Unit = {
    endGame = true
    arena.get.player.checkNewOwnRecord()
    GameState.endGame()
    savePlayerRankings()
    finishGame()
  }

  /** Notifies that the player has moved or shot.
   *
   *  @param action the [[Action]] notified by the view
   */
  def notifyAction(action: Action): Unit = {
    action.actionType match {
      case Movement => playerMoves = playerMoves :+ action.direction
      case Shoot => playerShoots = playerShoots :+ action.direction
    }
  }

  /** Notifies the transition to the game scene. */
  def onStartGame(): Unit = {
    require(view.isDefined)
    windowManager.scene_(new Intent(GameScene))
  }

  /** Notifies the transition to the classification scene. */
  override def onClassification(): Unit = {
    require(view.isDefined)
    view.get.windowManager.scene_(new Intent(PlayerRankingsScene))
  }

  /** Notifies the transition to the settings scene. */
  def onSettings(): Unit = {
    require(view.isDefined)
    windowManager.scene_(new Intent(SettingScene))
  }

  /** Notifies the transition to the credits scene. */
  def onCredits(): Unit = {
    require(view.isDefined)
    windowManager.scene_(new Intent(CreditsScene))
  }

  /** Notifies the intent to exit from game. */
  def onExit(): Unit = windowManager.closeView()

  /** Notifies to go back to the previous scene. */
  def onBack(): Unit = {
    require(view.isDefined)
    windowManager.scene_(new Intent(MainScene))
  }

  /** Notifies to save new game settings. */
  def onSave(settingPreferences: SettingPreferences): Unit = {
    require(view.isDefined)

    playerName_(settingPreferences.playerName)
    difficulty_(settingPreferences.difficulty)

    GameState.arenaWidth = settingPreferences.difficulty.arenaWidth
    GameState.arenaHeight = settingPreferences.difficulty.arenaHeight

    windowManager.showMessage("Save confirmation", "Settings saved successfully.", Info)
  }

  /** Loads player rankings from file */
  private def loadPlayerRankings(): Unit = {
    import utilities.ImplicitConversions._
    Using(Source.fromFile("rank.json")){ _.mkString } match {
      case Success(stringRank) =>
        allDifficulty.foreach( difficulty => {
          playerRankings = playerRankings ++ Map(difficulty.toString() -> ( for {
            JObject(playerRecord) <- parse(stringRank) \ difficulty
            JField("PlayerName", JString(name)) <- playerRecord
            JField("Record", JInt(record)) <- playerRecord
          } yield name -> record).toMap)
        })
      case Failure(_) =>
        allDifficulty.foreach(difficulty => playerRankings = playerRankings ++ Map(difficulty.toString() -> Map()))
    }
  }

  /** Saves player rankings into file */
  private def savePlayerRankings(): Unit = {
    import utilities.ImplicitConversions._
    playerRankings = playerRankings ++ Map(difficulty.toString -> playerRankings(difficulty).toSeq.sortWith(_._2 > _._2).toMap)

    implicit val formats: DefaultFormats.type = DefaultFormats
    val json = playerRankings.map { totalRanking =>
      totalRanking._1 -> totalRanking._2.map { player =>
        ("PlayerName" -> player._1) ~ ("Record" -> player._2)
      }
    }

    Some(new PrintWriter("rank.json")).foreach { file => file.write(JsonAST.prettyRender(json)); file.close() }
  }

  import utilities.ImplicitConversions._
  /** Loads the world record from the ranking */
  def loadWorldRecord(): Unit =
    worldRecord = if (playerRankings(difficulty).nonEmpty) playerRankings(difficulty).maxBy(_._2)._2 else 0

  override def startNewLevel(): Unit = GameState.nextLevel()

  def checkNewMovement(): Option[Direction] = {
    playerMoves.length match {
      case 0 => None
      case _ =>
        val direction = playerMoves.dequeue._1
        playerMoves = playerMoves.dequeue._2
        direction
    }
  }

  def checkNewShoot(): Option[Direction] = {
    playerShoots.length match {
      case 0 => None
      case _ =>
        val direction = playerShoots.dequeue._1
        playerShoots = playerShoots.dequeue._2
        direction
    }
  }

  /** Notifies player shoot */
  override def shoot(): Unit = play(ShootSound)

  /** Notifies when the player takes a collectible */
  override def takesCollectible(): Unit = play(BonusSound)

  /** Notifies when the player gets hurt */
  override def playerInjury(): Unit = play(InjurySound)

  /** Notifies when the door opens */
  override def openDoor(): Unit = play(LevelUpSound)

  /** Notifies when the player dies */
  override def playerDead(): Unit = play(FailureSound);  stopSound()

  /** Notifies when start new level */
  override def startGame(): Unit = println()//play(LevelMusic)

  /** Notifies end game */
  override def finishGame(): Unit = stopSound()
}