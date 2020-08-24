package gamemanager

import utilities.{Action, Direction, Intent, Movement, SettingPreferences, Shoot}
import handlers.PreferencesHandler._
import gameview.scene.{GameScene, SceneType}
import utilities.MessageTypes._
import SceneType._
import gamelogic.GameState._
import gameview.Window

import scala.collection.immutable.Queue
import gamelogic.MapGenerator._
import utilities.Difficulty._
import Runtime.getRuntime
import java.util.concurrent.Executors.newFixedThreadPool

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.fromExecutorService
import GameManager._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GameManager extends ViewObserver {
  lazy val windowManager: Window = view.get.windowManager

  /** Notifies that the game has started. */
  def notifyStartGame(): Unit = {
    startGame("Player1", gameType(Medium))
    ThreadPool.execute(new GameCycle())
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
}

object GameManager {
  val NumThreads: Int = getRuntime.availableProcessors() + 1
  val ThreadPool: ExecutionContext = fromExecutorService(newFixedThreadPool(NumThreads))
  val TimeSliceMillis: Int = 50
  var view: Option[GameScene] = None
  def view_(view: GameScene): Unit = this.view = Some(view)

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