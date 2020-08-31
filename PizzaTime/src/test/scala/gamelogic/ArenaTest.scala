package gamelogic

import Arena._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty._
import MapGenerator._
import GameState._

/** Test class for the behavior of [[Arena]]. */
class ArenaTest extends AnyFlatSpec with Matchers {
  "The Arena" can "be generated for a game" in {
    startGame("Player1", gameType(Medium))
    checkAllNonEmpty()
  }

  it should "have walls inside it" in {
    assert(arena.get.walls.nonEmpty)
    assert(arena.get.walls.forall(wall => checkBounds(wall.position.point, bounds = true)))
  }

  it should "have floor inside the walls" in {
    assert(arena.get.floor.nonEmpty)
    assert(arena.get.floor.forall(tile => checkBounds(tile.position.point)))
  }

  it should "have the player inside the walls" in {
    assert(checkBounds(arena.get.player.position.point))
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

  it can "be emptied" in {
    arena.get.emptyMap()
    checkAllEmpty()
  }

  private def checkAllEmpty(): Unit = {
    assert(arena.get.enemies.isEmpty)
    assert(arena.get.bullets.isEmpty)
    assert(arena.get.collectibles.isEmpty)
    assert(arena.get.obstacles.isEmpty)
  }

  private def checkAllNonEmpty(): Unit = {
    assert(arena.get.enemies.nonEmpty)
    assert(arena.get.bullets.nonEmpty)
    assert(arena.get.collectibles.nonEmpty)
    assert(arena.get.obstacles.nonEmpty)
  }
}