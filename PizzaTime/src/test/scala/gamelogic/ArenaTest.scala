package gamelogic

import Arena._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty._
import MapGenerator._

/** Test class for the behavior of [[Arena]].
 *  To ease testing, different [[Entity]]s are manually placed in the [[Player]]'s surroundings.
 */
class ArenaTest extends AnyFlatSpec with Matchers {
  val arena = new Arena("Player1", gameType(Medium))

  initializeSurroundings()

  "The Arena" should "have walls inside the playable area" in {
    assert(arena.walls.forall(wall => checkBounds(wall.position.point)))
  }

  it should "have floor inside the walls" in {
    assert(arena.floor.forall(tile => checkBounds(tile.position.point, innerBounds = true)))
  }

  it should "have collectibles inside the walls" in {
    assert(arena.collectibles.forall(collectible => checkBounds(collectible.position.point, innerBounds = true)))
  }

  it should "have obstacles inside the walls" in {
    assert(arena.obstacles.forall(obstacle => checkBounds(obstacle.position.point, innerBounds = true)))
  }

  it should "have the player inside the walls" in {
    assert(checkBounds(arena.player.position.point, innerBounds = true))
  }

  it should "have enemies inside the walls" in {
    assert(arena.enemies.forall(enemy => checkBounds(enemy.position.point, innerBounds = true)))
  }

  "Collectibles" should "be walkable" in {

  }

  private def initializeSurroundings(): Unit = ???
}