package GameManager

import GameManager.observers.ViewObserver

class GameManager extends ViewObserver {
  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = ???

  /** Notifies that there's a shoot */
  override def notifyShoot(): Unit = ???

  /** Notifies that the player has moved */
  override def notifyMovement(): Unit = ???

  /** Notifies the transition to the game scene */
  override def onStartGame(): Unit = ???

  /** Notifies the transition to the settings scene */
  override def onSettings(): Unit = ???

  /** Notifies the transition to the credits scene */
  override def onCredits(): Unit = ???

  /** Notifies the intent to exit from game */
  override def OnExit(): Unit = ???

  /** Notifies to go back to the previous scene */
  override def onBack(): Unit = ???
}
