package gamemanager

import gamelogic.GameState
import gamemanager.GameManager._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.{Action, Movement, Shoot, Up}

/**Test class for [[GameManager]]**/
class GameManagerTest extends AnyFlatSpec with Matchers {
  "When start game" should "be create a GameMap" in {
    notifyStartGame()
    GameState.arena must not be None
  }

  "When no movement is notified the queue of movement" should "empty" in {
    playerMoves.length must be (0)
  }

  "When a movement is notified" should "be added" in {
    notifyAction(Action (Movement, Some(Up)))
    playerMoves.length must be (1)
   }

  "When the loop checks for a new movement, if the movement is present" should "be returned" in {
    val newAction = checkNewMovement()
    newAction.get must be (Up)
  }

  "When no shoot is notified the queue of shoot" should "empty" in {
    playerShoots.length must be (0)
  }

  "When a shoot is notified" should "be added" in {
    notifyAction(Action (Shoot, Some(Up)))
    playerShoots.length must be (1)
  }

  "When the loop checks for a new shoot, if the shoot is present" should "be returned" in {
    val newAction = checkNewShoot()
    newAction.get must be (Up)
  }

  "When the loop checks for a new movement, if the movement isn't present" should "be return None" in {
    val newAction = checkNewMovement()
    newAction mustBe None
  }

  "When the loop checks for a new shoot, if the shoot isn't present" should "be return None" in {
    val newAction = checkNewShoot()
    newAction mustBe None
  }

  "When finish game endGame" should "true" in {
    notifyEndGame()
    endGame mustBe true
  }


}