package gamelogic

import gamelogic.Arena.center
import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.difficulty_
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, Up}

class BulletTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", gameType(Easy))
  val arena: GameMap = GameState.arena.get
  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()

  "A bullet" should "always move in the same direction it was shot" in {
    List(Up, Down, Left, Right).foreach(direction => {
      player.changeDirection(direction)
      nextStep(None, Some(direction))
      while (bullets.takeWhile(_.unexploded).nonEmpty) {
        bullets.foreach(bullet => bullet.position.dir shouldBe Some(direction))
        nextStep(None, None)
      }
    })
  }

  it should "explode after a while" in {
    nextStep(None, Some(Right))
    bullets.foreach(bullet => {
      for (_ <- 1 until bullet.range) {
        bullet.unexploded shouldBe true
        nextStep(None, None)
      }
      bullet.unexploded shouldBe false
    })
  }

}
