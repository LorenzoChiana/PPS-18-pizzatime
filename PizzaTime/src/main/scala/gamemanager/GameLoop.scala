package gamemanager

import gamelogic.GameState.arena
import gamelogic.MapGenerator._
import gamelogic.GameState._
import GameManager._
import utilities.Difficulty._
import gamelogic.{GameState, MapGenerator}
import gameview.fx.FXGameScene
import utilities.MessageTypes

class GameLoop() extends Thread  {

  def initGame(): Unit = {
    startGame("Player1", gameType(Medium))
    this.start()
  }

  override def run(): Unit = gameLoop()

  def gameLoop(): Unit = {
    while (!endGame) {
      val dialog = GameManager.view.get.windowManager

      nextStep(checkNewMovement(), checkNewShoot())
      numCycle = numCycle + 1

      if (arena.get.player.lives == 0) {
        //è da notificare anche al gameManager?
        dialog.showMessage("GAME OVER", "You lose", MessageTypes.Warning)
      }

      /** Update view */
      view.get match {
        case scene: FXGameScene => scene.updateView()
        case _ =>
      }
      Thread.sleep(80)
    }
    finishGame()
  }

  def finishGame() : Unit = println("Finish!")
}
