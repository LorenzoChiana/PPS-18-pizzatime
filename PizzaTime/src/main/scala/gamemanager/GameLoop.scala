package gamemanager

import java.lang.System.currentTimeMillis
import Thread.sleep

import GameManager._
import gamelogic.GameState.{arena, nextStep}
import gameview.fx.FXGameScene
import utilities.MessageTypes._

class GameLoop(gameManager: GameManager) extends Runnable  {
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
      gameManager.notifyEndGame()
      view.get.windowManager.showMessage("GAME OVER", "You lose", Warning)
    }

    /** Update view */
    view.get match {
      case scene: FXGameScene => if(arena.get.endedLevel) { scene.endLevel(); arena.get.endedLevel = false} else scene.updateView()
      case _ =>
    }

    if (!arena.get.player.isLive) endGame = true
  }

  def finishGame(): Unit = println("Finish!")
}
