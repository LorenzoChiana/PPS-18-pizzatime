package gamelogic

import Arena._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty._
import MapGenerator._
import utilities.Position

/** Test class for the behavior of [[Arena]].
 *  To ease testing, a dummy instance of [[Arena]] is manually populated with [[Entity]]s.
 */
class ArenaTest extends AnyFlatSpec with Matchers {
  val arena = new Arena("Player1", gameType(Medium))

    "The Arena" should "be empty after creation" in {
    assert(arena.allGameEntities.isEmpty)
  }

  it should "have walls inside it" in {
    assert(arena.walls.nonEmpty)
    assert(arena.walls.forall(wall => checkBounds(wall.position.point, bounds = true)))
  }

  it should "have floor inside the walls" in {
    assert(arena.floor.nonEmpty)
    assert(arena.floor.forall(tile => checkBounds(tile.position.point)))
  }

  it can "be populated with game entities" in {
    initializeDummyEntities()
    assert(arena.enemies.nonEmpty)
    assert(arena.bullets.nonEmpty)
    assert(arena.collectibles.nonEmpty)
    assert(arena.obstacles.nonEmpty)
  }

  it should "have collectibles inside the walls" in {
    assert(arena.collectibles.forall(collectible => checkBounds(collectible.position.point)))
  }

  it should "have obstacles inside the walls" in {
    assert(arena.obstacles.forall(obstacle => checkBounds(obstacle.position.point)))
  }

  it should "have the player inside the walls" in {
    assert(checkBounds(arena.player.position.point))
  }

  it should "have enemies inside the walls" in {
    assert(arena.enemies.forall(enemy => checkBounds(enemy.position.point)))
  }

  "Collectibles" should "be walkable" in {

  }

  private def initializeDummyEntities(): Unit = {
    arena.enemies = arena.enemies + Enemy(randomPosition)
    arena.bullets = arena.bullets + Bullet(randomPosition)
    arena.collectibles = arena.collectibles + BonusLife(randomPosition)
    arena.obstacles = arena.obstacles + Obstacle(randomPosition)
  }
}