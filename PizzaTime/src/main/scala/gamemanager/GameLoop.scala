package gamemanager

import java.lang.System.currentTimeMillis
import Thread.sleep
import GameManager._
import gamelogic.GameState.{arena, nextStep}
import gameview.fx.FXScene
import utilities.MessageTypes._

class GameLoop() extends Runnable  {
  def run(): Unit = {
    while (!endGame) {
      val startTime: Long = currentTimeMillis()

      gameStep()

      val deltaTime: Long = currentTimeMillis() - startTime
      if (deltaTime < TimeSliceMillis) sleep(TimeSliceMillis - deltaTime)
    }

    finishGame()
  }

  def gameStep(): Unit = {
    nextStep(checkNewMovement(), checkNewShoot())
    numCycle += 1

    if (!arena.get.player.isLive) {
      //è da notificare anche al gameManager?
      view.get.windowManager.showMessage("GAME OVER", "You lose", Warning)
      endGame = true
    }

    /** Update view */
    view.get match {
      case scene: FXScene => scene.updateView()
      case _ =>
    }
  }

  def finishGame(): Unit = println("Finish!")
}
