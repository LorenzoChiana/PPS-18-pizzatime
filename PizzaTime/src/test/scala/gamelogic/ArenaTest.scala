package gamelogic

import Arena._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty._
import MapGenerator._
import GameState._

/** Test class for the behavior of [[Arena]].
 *  To ease some tests, a dummy instance of [[Arena]] is also created.
 */
class ArenaTest extends AnyFlatSpec with Matchers {
  val arenaDummy: GameMap = Arena("Player1", gameType(Easy))

  "The Arena" should "be empty after creation" in {
    assert(arenaDummy.allGameEntities.isEmpty)
  }

  it should "have walls inside it" in {
    assert(arenaDummy.walls.nonEmpty)
    assert(arenaDummy.walls.forall(wall => checkBounds(wall.position.point, bounds = true)))
  }

  it should "have floor inside the walls" in {
    assert(arenaDummy.floor.nonEmpty)
    assert(arenaDummy.floor.forall(tile => checkBounds(tile.position.point)))
  }

  it should "have the player inside the walls" in {
    assert(checkBounds(arenaDummy.player.position.point))
  }

  it can "be populated with game entities" in {
    startGame("Player1", gameType(Easy))
    assert(arena.get.enemies.nonEmpty)
    assert(arena.get.collectibles.nonEmpty)
    assert(arena.get.obstacles.nonEmpty)
  }

  it should "have enemies inside the walls" in {
    assert(arena.get.enemies.forall(enemy => checkBounds(enemy.position.point)))
  }

  it should "have bullets inside the walls" in {
    assert(arena.get.bullets.forall(bullet => checkBounds(bullet.position.point)))
  }

  it should "have collectibles inside the walls" in {
    assert(arena.get.collectibles.forall(collectible => checkBounds(collectible.position.point)))
  }

  it should "have obstacles inside the walls" in {
    assert(arena.get.obstacles.forall(obstacle => checkBounds(obstacle.position.point)))
  }
}