package gamemanager

import utilities.{Action, SettingPreferences}

/** Represents an observer for the view */
trait ViewObserver {

  /** Notifies that the game has started */
  def notifyStartGame(): Unit

  /** Notifies that the player has moved or shoot */
  def notifyAction(action: Action): Unit

  /** Notifies that the game has ended */
  def notifyEndGame(): Unit

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

  /** Saves preferences */
  def onSave(settingPreferences: SettingPreferences): Unit

  def startNewLevel(): Unit

}
