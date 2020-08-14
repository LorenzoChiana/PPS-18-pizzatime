package gamemanager

import gamelogic.GameState
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXGameScene

import scala.collection.mutable.ListBuffer
import utilities.Direction
/*ci andrà anche la view quando sarà presente*/

class GameLoop()  extends Thread  {

  def initGame():Unit = {
    GameState.startGame(PreferencesHandler.playerName, gameType(PreferencesHandler.difficulty))
    this.start()
  }

  override def run(): Unit = gameLoop()

  def gameLoop(): Unit = if (GameManager.endGame) {
    finishGame()
  } else {
 //   GameState.nextStep(GameManager.checkNewMovement())
    GameManager.numCycle = GameManager.numCycle + 1

    /** Update view */
    if (GameManager.view.get.isInstanceOf[FXGameScene])
      GameManager.view.get.asInstanceOf[FXGameScene].updateView()

    Thread.sleep(80)
    gameLoop()
  }

  def finishGame() : Unit = println("Finish!")
}
