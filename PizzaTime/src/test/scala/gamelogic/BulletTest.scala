package gamelogic

import gamelogic.Entity.nearPoint
import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, Up}

class BulletTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", gameType(Easy))
  val centerPoint: Point = Point(difficulty.arenaWidth/2, difficulty.arenaHeight/2)
  val arena: GameMap = GameState.arena.get
  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()

  "A bullet" should "always move in the same direction it was shot" in {
    List(Up, Down, Left, Right).foreach(direction => {
      player.changeDirection(direction)
      nextStep(None, Some(direction))
      while (bullets.exists(_.canMove)) {
        bullets.foreach(bullet => bullet.position.dir shouldBe Some(direction))
        nextStep(None, None)
      }
    })
  }

  it should "explode after a while" in {
    List(Up, Down, Left, Right).foreach(direction => nextStep(None, Some(direction)))
    bullets.foreach(_.unexploded shouldBe true)
    while(bullets.exists(_.unexploded) || bullets.exists(_.canMove)) nextStep(None, None)
    bullets.foreach(_.unexploded shouldBe false)
  }

  val entityOfCollisionPosition: Position = Position(nearPoint(nearPoint(centerPoint, Right), Right), Option(Right))

  it should "explode if it collides with an obstacle" in checkExplosionWhenCollides(Obstacle(entityOfCollisionPosition), mustExplode = true)

  it should "explode if it collides with an enemy" in checkExplosionWhenCollides(Enemy(entityOfCollisionPosition), mustExplode = true)

  it should "explode if it collides with a wall" in checkExplosionWhenCollides(Wall(entityOfCollisionPosition), mustExplode = true)

  it should "not explode is it collides with a collectible" in {
    checkExplosionWhenCollides(BonusScore(entityOfCollisionPosition, 5), mustExplode = false)
    checkExplosionWhenCollides(BonusLife(entityOfCollisionPosition), mustExplode = false)
  }

  private def checkExplosionWhenCollides(entity: Entity, mustExplode: Boolean): Unit = {
    player moveTo Position(centerPoint, Some(Right))
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
