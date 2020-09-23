package gamelogic

import GameState.nextStep
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Position.changePosition
import utilities.{Direction, Down, Left, Position, Right, StaticArena, Up}

/** Test class for [[Bullet]]. */
class BulletTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialHeroPosition = Position(Arena.center, Some(Up)),
    initialEnemyPosition = changePosition(changePosition(Position(Arena.center, Some(Up)), Up), Up),
    obstaclePosition = changePosition(changePosition(Position(Arena.center, Some(Up)), Right), Right),
    wallPosition = changePosition(changePosition(Position(Arena.center, Some(Up)), Down), Down)
  )
  import staticArena.arena._
  import staticArena._

  "A Bullet" should "explode after a while" in {
    nextStep(None, Some(Left))
    bullets.foreach(_.unexploded shouldBe true)
    while(bullets.exists(_.unexploded) || bullets.exists(_.bulletRange > 0)) nextStep(None, None)
    bullets.foreach(_.unexploded shouldBe false)
  }

  it should "always move in the same direction it was shot" in {
    nextStep(None, Some(Left))
    while(bullets.exists(_.unexploded)) {
      bullets.foreach(bullet => bullet.position.dir shouldBe Some(Left))
      nextStep(None, None)
    }
  }

  val entityOfCollisionPosition: Position = changePosition(Position(Arena.center, Some(Up)), Right)

  it should "explode if it collides with an obstacle" in {
    checkExplosionWhenCollides(obstaclePosition, Right)
  }

  it should "explode if it collides with an enemy" in {
    checkExplosionWhenCollides(initialEnemyPosition, Up)
  }

  it should "explode if it collides with a wall" in {
    checkExplosionWhenCollides(wallPosition, Down)
  }

  private def checkExplosionWhenCollides(position: Position, direction: Direction): Unit = {
    nextStep(None, Some(direction))
    while(bullets.exists(_.unexploded)) nextStep(None, None)
    bullets.filter(!_.unexploded).foreach(_.position.point shouldBe position.point)
  }
}
