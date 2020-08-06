package gamemanager


import utilities.Direction
import gamemanager.observers.ViewObserver
import gameview.Window
import scala.language.postfixOps
import scala.collection.mutable.ListBuffer

class GameManager(view: Window) extends ViewObserver {
  //Flag per controllare la fine del gioco
  val endGame : Boolean = false
  //Array che tiene traccia di movimenti
  val playerMoves = ListBuffer[Direction]()
  //Variabile che tiene traccia degli spari
  var playerShoots : Int = 0

  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = {
    val gameCycle : GameCycle = new GameCycle(this)
    gameCycle initGame;
    gameCycle run
  }

  /** Notifies that there's a shoot */
  override def notifyShoot(): Unit =  playerShoots = playerShoots + 1

  /** Notifies that the player has moved */
  override def notifyMovement(direction: Direction): Unit = {
    playerMoves :+ direction
  }

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
