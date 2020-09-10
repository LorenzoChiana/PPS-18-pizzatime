package gamemanager

/** Represents an observer for model */
trait GameLogicObserver {

  /** Notifies player shoot */
  def shoot()

  /** Notifies when the player takes a collectible */
  def takesCollectible()

  /** Notifies when the player gets hurt*/
  def playerInjury()

  /** Notifies when the door opens*/
  def openDoor()

  /** Notifies when the player dies*/
  def playerDead()

  /** Notifies start game*/
  def startGame()

  /** Notifies end game*/
  def finishGame()
}
