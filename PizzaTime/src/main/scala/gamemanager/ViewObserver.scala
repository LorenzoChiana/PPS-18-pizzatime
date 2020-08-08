package gamemanager

import utilities.SettingPreferences

/** Represents an observer for the view */
trait ViewObserver {

  /** Notifies that the game has started */
  def notifyStartGame(): Unit

  /** Notifies that there's a shoot */
  def notifyShoot(): Unit

  /** Notifies that the player has moved */
  def notifyMovement(): Unit

  /** Notifies the transition to the game scene */
  def onStartGame(): Unit

  /** Notifies the transition to the settings scene */
  def onSettings(): Unit

  /** Notifies the transition to the credits scene */
  def onCredits(): Unit

  /** Notifies the intent to exit from game */
  def onExit(): Unit

  /** Notifies to go back to the previous scene */
  def onBack(): Unit

  def notifySettings(): Unit

  def onSave(settingPreferences: SettingPreferences): Unit

}
