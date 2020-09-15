package gamelogic

import Entity._
import GameState.nextStep
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.{Down, Left, Position, Right, Up}

/** Test class for [[Bullet]] */
class BulletTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialPlayerPosition = Position(Arena.center, Some(Up))
  )
  import staticArena.arena._

  "A Bullet" should "explode after a while" in {
    List(Up, Down, Left, Right).foreach(direction => nextStep(None, Some(direction)))
    bullets.foreach(_.unexploded shouldBe true)
    while(bullets.exists(_.unexploded) || bullets.exists(_.canMove)) nextStep(None, None)
    bullets.foreach(_.unexploded shouldBe false)
  }

  val entityOfCollisionPosition: Position = Position(nearPoint(nearPoint(Arena.center, Right), Right), Option(Right))

  it should "explode if it collides with an obstacle" in {
    checkExplosionWhenCollides(
      Obstacle(entityOfCollisionPosition),
      mustExplode = true
    )
  }

  it should "explode if it collides with an enemy" in {
    checkExplosionWhenCollides(
      Enemy(entityOfCollisionPosition),
      mustExplode = true
    )
  }

  it should "explode if it collides with a wall" in {
    checkExplosionWhenCollides(
      Wall(entityOfCollisionPosition),
      mustExplode = true
    )
  }

  it should "not explode is it collides with a collectible" in {
    checkExplosionWhenCollides(
      BonusScore(entityOfCollisionPosition, 5),
      mustExplode = false
    )
    checkExplosionWhenCollides(
      BonusLife(entityOfCollisionPosition),
      mustExplode = false
    )
  }

  it should "always move in the same direction it was shot" in {
    List(Up, Down, Left, Right).foreach(direction => {
      nextStep(None, Some(direction))
      while (bullets.exists(_.unexploded)) {
        bullets.foreach(bullet => bullet.position.dir shouldBe Some(direction))
        nextStep(None, None)
      }
    })
  }

  private def checkExplosionWhenCollides(entity: Entity, mustExplode: Boolean): Unit = {
    nextStep(None, Some(Right))
    while(bullets.exists(_.unexploded)) nextStep(None, None)
    bullets.filter(!_.unexploded).foreach(bullet =>
      if (mustExplode)
        bullet.position.point shouldBe entity.position.point
      else
        bullet.position.point should not be entity.position.point
    )
    entity.remove()
  }
}
