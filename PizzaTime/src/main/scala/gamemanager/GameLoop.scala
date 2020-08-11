package gamemanager

import gamelogic.GameState

import scala.collection.mutable.ListBuffer
import utilities.Direction
/*ci andrà anche la view quando sarà presente*/

class GameLoop()  extends Thread  {

  def initGame():Unit = {
    GameState.startGame("Player1")
    this.start()
  }

  override def run(): Unit = {
    notifyModelDoStep()

    if (GameManager.playerShoots > 0 )  notifyModelPlayerShoot()
    if (!GameManager.playerMoves.isEmpty) notifyModelPlayerMoves(GameManager.playerMoves); GameManager.playerMoves.clear()

    //Update view
  }
  /*metodo che contiene il ciclo dell'attuale partita:
  *
  * dice al model di andare avanti di uno step
  * controlla se è successo qualcosa (sparato/mosso)
  * update view
  * */

  def notifyModelDoStep() : Unit = ???
  def notifyModelPlayerMoves(direction: ListBuffer[Direction]) : Unit = ???
  def notifyModelPlayerShoot() : Unit = ???
}
