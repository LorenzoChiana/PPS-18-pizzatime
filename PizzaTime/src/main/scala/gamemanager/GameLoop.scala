package gamemanager

import java.lang.System.currentTimeMillis
import Thread.sleep

import GameManager._
import gamelogic.GameState.{arena, nextStep}
import gameview.fx.FXGameScene

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

    /** Update view */
    if (view.nonEmpty)
      view.get match {
        case scene: FXGameScene => if(arena.get.endedLevel) {scene.endLevel(); arena.get.endedLevel = false} else {scene.updateView()}
        case _ =>
      }

    if (arena.get.player.isDead) notifyEndGame()
  }

  def finishGame(): Unit = println("Finish!")
}
