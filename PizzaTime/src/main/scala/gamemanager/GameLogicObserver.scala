package gamemanager

/** Represents an observer for model */
trait GameLogicObserver {

  /** Notifies player shoot */
  def shoot(): Unit

  /** Notifies when the player takes a collectible */
  def takesCollectible(): Unit

  /** Notifies when the player gets hurt*/
  def playerInjury(): Unit

  /** Notifies when the door opens*/
  def openDoor(): Unit

  /** Notifies when the player dies*/
  def playerDead(): Unit

  /** Notifies start game*/
  def startGame(): Unit

  /** Notifies end game*/
  def finishGame(): Unit
}
