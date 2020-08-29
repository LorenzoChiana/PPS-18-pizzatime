package gamemanager

import java.lang.System.currentTimeMillis
import Thread.sleep

import gamelogic.GameState._
import GameManager._
import gamelogic.Arena
import gameview.fx.FXGameScene
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

    println(Arena.exitLevel())

    /** Update view */
    view.get match {
      case scene: FXGameScene => if(Arena.exitLevel()) { scene.endLevel(); println("exit level")} else scene.updateView()
      case _ =>
    }

    if (!arena.get.player.isLive) endGame = true
  }

  def finishGame(): Unit = println("Finish!")
}
