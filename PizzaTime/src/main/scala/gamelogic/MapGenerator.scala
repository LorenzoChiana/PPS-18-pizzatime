package gamelogic

import MapGenerator._
import Arena._
import utilities.{Difficulty, Down, Position}
import utilities.Difficulty._
import GameState._
import utilities.ImplicitConversions._
import scala.annotation.tailrec
import scala.util.Random.{between, nextInt}

/** Encapsulates the logic for generating a new level.
 *  Used by [[Arena]].
 *
 *  @param difficulty the [[Difficulty]] chosen by the user
 */
case class MapGenerator(difficulty: Difficulty.Value) {
  var currentLevel: Int = 0

  /** Generates a new level, populating the [[Arena]] with the resulting [[Entity]]s. */
  def generateLevel(): Unit = {
    currentLevel += 1
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = {
    val enemyNum: Int = between(difficulty.malusRange.min * levelMultiplier, difficulty.malusRange.max * levelMultiplier)

    for (_ <- 0 to enemyNum) {
      arena.get.enemies = arena.get.enemies + Enemy(randomPosition)
    }
  }

  private def generateCollectibles(): Unit = {
    val bonusNum: Int = between(difficulty.bonusRange.min, difficulty.bonusRange.max)

    for (_ <- 0 to bonusNum) {
      val bonus: Collectible = if (nextInt(2) == 0) BonusLife(randomPosition) else BonusScore(randomPosition, difficulty.bonusScore)
      arena.get.collectibles = arena.get.collectibles + bonus
    }
  }

  private def generateObstacles(): Unit = {
    val obstaclesNum: Int = between(difficulty.malusRange.min, difficulty.malusRange.max)
    val obstacleDim: Int = between(difficulty.obstacleDimension.min, difficulty.obstacleDimension.max)

    for (_ <- 0 to obstaclesNum) {
      randomPositions(obstacleDim).foreach(p => arena.get.obstacles = arena.get.obstacles + Obstacle(p))
    }
  }

  def levelMultiplier: Int = (arena.get.mapGen.currentLevel / difficulty.levelThreshold) + 1
}

/** Utility methods for [[MapGenerator]]. */
object MapGenerator {
  /** Creates a [[MapGenerator]].
   *
   *  @param difficulty the [[Difficulty]] chosen by the user
   *  @return the new [[MapGenerator]] instance
   */
  def gameType(difficulty: DifficultyVal): MapGenerator = new MapGenerator(difficulty)

  /** Returns a random and clear [[Position]] on the [[Arena]]. */
  @tailrec
  def randomPosition: Position = {
    val x = between(1, arenaWidth - 1)
    val y = between(1, arenaHeight - 1)

    if (isClearFloor(x, y)) Position((x, y), Some(Down)) else randomPosition
  }

  /** Returns a set of adjacent, random and clear [[Position]]s on the [[Arena]].
   *
   * @param dim the number of [[Position]]s
   */
  @tailrec
  def randomPositions(dim: Int): Set[Position] = {
    val startingPosition: Position = randomPosition
    var obstacles: Set[Position] = Set(startingPosition)

    surroundings(startingPosition.point).foreach(p => {
      if (isClearFloor(p) && (obstacles.size < dim)) {
        obstacles = obstacles + Position(p, Some(Down))
      }
    })

    if (obstacles.size == dim) obstacles else randomPositions(dim)
  }
}