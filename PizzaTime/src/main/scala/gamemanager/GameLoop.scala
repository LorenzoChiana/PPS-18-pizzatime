package gamemanager

import scala.collection.mutable.ListBuffer
import utilities.Direction
/*ci andrà anche la view quando sarà presente*/

class GameCycle(gameManager: GameManager)  extends Thread  {

  def initGame():Unit = ???

  override def run(): Unit = {
    notifyModelDoStep()

    if (gameManager.playerShoots > 0 )  notifyModelPlayerShoot()
    if (!gameManager.playerMoves.isEmpty) notifyModelPlayerMoves(gameManager.playerMoves); gameManager.playerMoves.clear()

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
