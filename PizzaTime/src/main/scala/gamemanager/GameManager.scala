package gamemanager

import utilities.{Action, Difficulty, Direction, Intent, Movement, SettingPreferences, Shoot}
import handlers.PreferencesHandler._
import gameview.scene.{Scene, SceneType}
import utilities.MessageTypes._
import SceneType._
import gameview.Window

import scala.collection.immutable.Queue
import gamelogic.MapGenerator._
import utilities.Difficulty._
import Runtime.getRuntime
import java.io.PrintWriter
import java.util.concurrent.Executors.newFixedThreadPool

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.fromExecutorService
import GameManager._
import gamelogic.GameState
import gamelogic.GameState.arena
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonAST.JNull.\
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import scala.io.Source
import scala.util.{Failure, Success, Using}

class GameManager extends ViewObserver {
  lazy val windowManager: Window = view.get.windowManager

  loadPlayerRankings()

  /** Notifies that the game has started. */
  def notifyStartGame(): Unit = {
    GameState.startGame("Player1", gameType(Medium))
    ThreadPool.execute(new GameLoop())
  }

  /** Notifies that the game has ended */
  def notifyEndGame(): Unit = {
    endGame = true
    GameState.endGame()
    savePlayerRankings()
  }

  /** Notifies that the player has moved or shot.
   *
   *  @param action the [[Action]] notified by the view
   */
  def notifyAction(action: Action): Unit = action.actionType match{
    case Movement => playerMoves = playerMoves :+ action.direction
    case Shoot => playerShoots = playerShoots :+ arena.get.player.position.dir
  }

  /** Notifies the transition to the game scene. */
  def onStartGame(): Unit = {
    require(view.isDefined)
    windowManager.scene_(new Intent(GameScene))
  }

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

    windowManager.showMessage("Save confirmation", "Settings saved successfully.", Info)
  }

  private def loadPlayerRankings(): Unit = {
    import utilities.ImplicitConversions._
    Using(Source.fromFile("rank.json")){ _.mkString } match {
      case Success(stringRank) =>
        Difficulty.allDifficulty.foreach( d => {
          GameState.playerRankings = GameState.playerRankings ++ Map(d.toString() -> (for {
            JObject(playerRecord) <- parse(stringRank) \ d.toString()
            JField("PlayerName", JString(name)) <- playerRecord
            JField("Record", JInt(record)) <- playerRecord
          } yield name -> record).toMap)
        })
      case Failure(error) => println("Error: " + error)
    }
  }

  private def savePlayerRankings(): Unit = {
    //GameState.playerRankings.foreach = Map(difficulty.toString -> GameState.playerRankings(difficulty).toSeq.sortWith(_._2 > _._2).toMap)

    implicit val formats: DefaultFormats.type = DefaultFormats
    val json = GameState.playerRankings.map { totalRanking =>
      totalRanking._1 -> totalRanking._2.map { player =>
        ("PlayerName" -> player._1) ~ ("Record" -> player._2)
      }
    }

    /* val json2 = difficulty.toString ->
       GameState.playerRankings.map { player =>
         ("PlayerName" -> player._1) ~ ("Record" -> player._2)
       }*/

    Some(new PrintWriter("rank.json")).foreach { file => file.write(JsonAST.prettyRender(json)); file.close() }
  }

  override def startNewLevel(): Unit = {
    GameState.nextLevel()
  }
}

object GameManager {
  val NumThreads: Int = getRuntime.availableProcessors() + 1
  val ThreadPool: ExecutionContext = fromExecutorService(newFixedThreadPool(NumThreads))
  val TimeSliceMillis: Int = 50
  var view: Option[Scene] = None
  def view_(view: Scene): Unit = this.view = Some(view)

  var numCycle: Int = 0

  var endGame: Boolean = false

  /** [[Queue]] for movements notified but not yet processed. */
  var playerMoves: Queue[Option[Direction]] = Queue[Option[Direction]]()

  /** [[Queue]] for shoots notified but not yet processed. */
  var playerShoots: Queue[Option[Direction]] = Queue[Option[Direction]]()

  def checkNewMovement(): Option[Direction] = playerMoves.length match {
    case 0 => None
    case _ =>
      val direction = playerMoves.dequeue._1
      playerMoves = playerMoves.dequeue._2
      direction
  }

  def checkNewShoot(): Option[Direction] = playerShoots.length match {
    case 0 => None
    case _ =>
      val direction = playerShoots.dequeue._1
      playerShoots = playerShoots.dequeue._2
      direction
  }
}