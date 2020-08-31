package gamemanager

import java.lang.System.currentTimeMillis
import Thread.sleep

import gamelogic.GameState._
import GameManager._
import gameview.fx.FXGameScene

class GameLoop() extends Runnable  {
  def run(): Unit = {
    while (!endGame) {
      val startTime: Long = currentTimeMillis()

      gameStep()

      val deltaTime: Long = currentTimeMillis() - startTime
      if (deltaTime < TimeSliceMillis) sleep(TimeSliceMillis - deltaTime)
      println("dopo delta time")
    }

    finishGame()
  }

  def gameStep(): Unit = {
    println("inizio game step")
    nextStep(checkNewMovement(), checkNewShoot())
    println("dopo next step")
    numCycle += 1

    /** Update view */
    view.get match {
      case scene: FXGameScene => if(arena.get.endedLevel) { scene.endLevel(); arena.get.endedLevel = false; println("endlevel")} else { scene.updateView(); println("false")}
      case _ =>
    }

    println("dopo updateView")
    if (!arena.get.player.isLive) endGame = true
  }

  def finishGame(): Unit = println("Finish!")
}
