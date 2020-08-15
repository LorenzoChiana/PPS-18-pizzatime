package gamemanager

import gameview.fx.FXGameScene
import gamelogic.{GameState, MapGenerator}

class GameLoop() extends Thread  {

  def initGame(): Unit = {
    GameState.startGame("Player1", new MapGenerator(0, 0))
    this.start()
  }

  override def run(): Unit = gameLoop()

  def gameLoop(): Unit = if (GameManager.endGame) {
    finishGame()
  } else {
    GameState.nextStep(GameManager.checkNewMovement())
    GameManager.numCycle = GameManager.numCycle + 1

    /** Update view */
    if (GameManager.view.get.isInstanceOf[FXGameScene])
      GameManager.view.get.asInstanceOf[FXGameScene].updateView()

    Thread.sleep(80)
    gameLoop()
  }

  def finishGame() : Unit = println("Finish!")
}
