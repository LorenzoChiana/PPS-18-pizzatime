package gamelogic

import Arena._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty._
import MapGenerator._
import GameState._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.enablers.Emptiness.emptinessOfGenTraversable

/** Test class for the behavior of [[Arena]]. */
class ArenaTest extends AnyFlatSpec with Matchers {
  startGame("Player1", gameType(Medium))

  val arena: GameMap = GameState.arena.get
  import arena._

  "The Arena" can "be generated for a game" in {
    endedLevel shouldBe false
    enemies should not be empty
    collectibles should not be empty
    obstacles should not be empty
  }

  it should "not be too full" in {
    checkArenaPopulation() shouldBe true
  }

  it should "have walls inside it" in {
    walls should not be empty
    walls.forall(wall => checkBounds(wall.position.point, bounds = true)) shouldBe true
  }

  it should "have floor inside the walls" in {
    floor should not be empty
    floor.forall(tile => checkBounds(tile.position.point)) shouldBe true
  }

  it should "have the player inside the walls" in {
    checkBounds(player.position.point) shouldBe true
  }

  it should "have enemies inside the walls" in {
    enemies.forall(enemy => checkBounds(enemy.position.point)) shouldBe true
  }

  it should "have bullets inside the walls" in {
    bullets.forall(bullet => checkBounds(bullet.position.point)) shouldBe true
  }

  it should "have collectibles inside the walls" in {
    collectibles.forall(collectible => checkBounds(collectible.position.point)) shouldBe true
  }

  it should "have obstacles inside the walls" in {
    obstacles.forall(obstacle => checkBounds(obstacle.position.point)) shouldBe true
  }

  it can "be emptied" in {
    emptyMap()
    enemies shouldBe empty
    bullets shouldBe empty
    collectibles shouldBe empty
    obstacles shouldBe empty
  }

  it can "be generated again" in {
    mapGen.generateLevel()
    endedLevel shouldBe false
    enemies should not be empty
    collectibles should not be empty
    obstacles should not be empty
    checkArenaPopulation() shouldBe true
  }

  "Enemies" should "be in their range" in {
    enemies.size shouldBe (mapGen.difficulty.enemiesRange.min * mapGen.levelMultiplier) +- (mapGen.difficulty.enemiesRange.max * mapGen.levelMultiplier)
  }

  "Collectibles" should "be in their range" in {
    collectibles.size shouldBe mapGen.difficulty.collectiblesRange.min +- mapGen.difficulty.collectiblesRange.max
  }

  "Obstacles" should "be in their range" in {
    obstacles.size shouldBe mapGen.difficulty.obstaclesRange.min +- mapGen.difficulty.obstaclesRange.max
  }
}