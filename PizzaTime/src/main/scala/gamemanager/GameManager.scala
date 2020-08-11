package gamemanager

import utilities.Direction
import scala.language.postfixOps
import scala.collection.mutable.ListBuffer
import gamemanager.handlers.PreferencesHandler
import gameview.scene.{GameScene, SceneType}
import utilities.MessageTypes.Info
import utilities.{Intent, SettingPreferences}
import SceneType._
import gameview.Window

class GameManager extends ViewObserver {
  lazy val windowManager: Window = GameManager.view.get.windowManager

  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = {
    val gameCycle: GameLoop = new GameLoop()
    gameCycle initGame;
    gameCycle run
  }

  /** Notifies that there's a shoot */
  override def notifyShoot(): Unit = GameManager.playerShoots = GameManager.playerShoots + 1

  /** Notifies that the player has moved */
  override def notifyMovement(direction: Direction): Unit = {
    GameManager.playerMoves :+ direction
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
  //Flag for the end of game
  var endGame: Boolean = false
  //Array che tiene traccia di movimenti
  val playerMoves = ListBuffer[Direction]()
  //Variabile che tiene traccia degli spari
  var playerShoots: Int = 0
  var numCycle: Int = 0
}