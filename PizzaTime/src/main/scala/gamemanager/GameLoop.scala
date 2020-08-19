package gamemanager

import gameview.fx.FXGameScene
import gamelogic.MapGenerator._
import gamelogic.GameState._
import GameManager._
import utilities.Difficulty._

class GameLoop() extends Thread  {

  def initGame(): Unit = {
    startGame("Player1", gameType(Medium))
    this.start()
  }

  override def run(): Unit = gameLoop()

  def gameLoop(): Unit = if (endGame) {
    finishGame()
  } else {
    nextStep(checkNewMovement())
    numCycle += 1

    /** Update view */
    if (view.get.isInstanceOf[FXGameScene])
      view.get.asInstanceOf[FXGameScene].updateView()

    Thread.sleep(80)
    gameLoop()
  }

  def finishGame() : Unit = println("Finish!")
}
