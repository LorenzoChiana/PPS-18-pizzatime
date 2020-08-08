package gamemanager

import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXSettingsScene
import gameview.scene.{Scene, SceneType}
import utilities.MessageTypes.Info
import utilities.{Intent, SettingPreferences}
import SceneType._
import gameview.Window

class GameManager extends ViewObserver {

  var endGame: Boolean = false
  var numCycle: Int = 0
  lazy val windowManager: Window = GameManager.view.get.windowManager

  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = ???

  /** Notifies that there's a shoot */
  override def notifyShoot(): Unit = ???

  /** Notifies that the player has moved */
  override def notifyMovement(): Unit = ???

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
  var view: Option[Scene] = None

  def view_(view: Scene): Unit = this.view = Some(view)
}