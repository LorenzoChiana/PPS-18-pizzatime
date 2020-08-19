package gamemanager

import gamelogic.GameState.arena
import gameview.fx.FXGameScene
import gamelogic.{GameState, MapGenerator}
import utilities.MessageTypes

class GameLoop() extends Thread  {

  def initGame(): Unit = {
    GameState.startGame("Player1", new MapGenerator(0, 0))
    this.start()
  }

  override def run(): Unit = gameLoop()

  def gameLoop(): Unit = {
    val dialog = GameManager.view.get.windowManager

    GameState.nextStep(GameManager.checkNewMovement())
    GameManager.numCycle = GameManager.numCycle + 1

    if (arena.get.player.lives == 0) {
      //è da notificare anche al gameManager?
      dialog.showMessage("GAME OVER", "You lose", MessageTypes.Warning)
    }

    /** Update view */
    if (GameManager.view.get.isInstanceOf[FXGameScene])
      GameManager.view.get.asInstanceOf[FXGameScene].updateView()

    Thread.sleep(80)
    gameLoop()
  }

  def finishGame() : Unit = println("Finish!")
}
