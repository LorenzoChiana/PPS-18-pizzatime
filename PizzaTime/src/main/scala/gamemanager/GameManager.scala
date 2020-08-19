package gamemanager

import utilities.{Action, Direction, Intent, Movement, SettingPreferences, Shoot}

import scala.language.postfixOps
import gamemanager.handlers.PreferencesHandler
import gameview.scene.{GameScene, SceneType}
import utilities.MessageTypes.Info
import SceneType._
import gamelogic.GameState.arena
import gameview.Window

import scala.collection.immutable.Queue

class GameManager extends ViewObserver {
  lazy val windowManager: Window = GameManager.view.get.windowManager

  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = {
    val gameCycle: GameLoop = new GameLoop()
    gameCycle.initGame()
  }

  /**
   * Notifies that the player has moved or shoot
   * @param action the [[Action]] notified by the view
   * */
  override def notifyAction(action: Action): Unit = action.actionType match{
    case Movement => GameManager.playerMoves = GameManager.playerMoves :+ action.direction
    case Shoot => GameManager.playerShoots = GameManager.playerShoots :+ arena.get.player.position.dir
  }

  override def notifySettings(): Unit = ???

  /** Notifies the transition to the game scene */
  override def onStartGame(): Unit = {
    require(GameManager.view.isDefined)
    windowManager.scene_(new Intent(GameScene))
  }

  /** Notifies the transition to the settings scene */
  override def onSettings(): Unit = {
    require(GameManager.view.isDefined)
    windowManager.scene_(new Intent(SettingScene))
  }

  /** Notifies the transition to the credits scene */
  override def onCredits(): Unit = {
    require(GameManager.view.isDefined)
    windowManager.scene_(new Intent(CreditsScene))
  }

  /** Notifies the intent to exit from game */
  override def onExit(): Unit = windowManager.closeView()

  /** Notifies to go back to the previous scene */
  override def onBack(): Unit = {
    require(GameManager.view.isDefined)
    windowManager.scene_(new Intent(MainScene))
  }

  /** Notifies to save new game settings */
  override def onSave(settingPreferences: SettingPreferences): Unit = {
    require(GameManager.view.isDefined)

    PreferencesHandler.playerName_(settingPreferences.playerName)
    PreferencesHandler.difficulty_(settingPreferences.difficulty)

    windowManager.showMessage("Save confirmation", "Settings saved successfully.", Info)
  }


}

object GameManager {
  var view: Option[GameScene] = None
  def view_(view: GameScene): Unit = this.view = Some(view)

  /**
   * Flag for the end of game
   */
  var endGame: Boolean = false

  /**
   * [[Queue]] for movements notified but not yet processed
   */

  var playerMoves: Queue[Option[Direction]] = Queue[Option[Direction]]()
  /**
   * [[Queue]] for shoots notified but not yet processed
   */
  var playerShoots: Queue[Option[Direction]] = Queue[Option[Direction]]()

  var numCycle: Int = 0

  def checkNewMovement(): Option[Direction] = playerMoves.length match {
    case 0 => None
    case _ => {
      val direction = playerMoves.dequeue._1
      playerMoves = playerMoves.dequeue._2
      direction
    }
  }

  def checkNewShoot(): Option[Direction] = playerShoots.length match {
    case 0 => None
    case _ => {
      val direction = playerShoots.dequeue._1
      playerShoots = playerShoots.dequeue._2
      direction
    }
  }
}