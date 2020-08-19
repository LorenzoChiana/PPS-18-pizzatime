package gamelogic

import MapGenerator._
import Arena._
import utilities.{Difficulty, Down, Position}
import utilities.Difficulty._
import GameState._
import utilities.ImplicitConversions._

import scala.util.Random
import scala.util.Random.between

/** Encapsulates the logic for generating a new level.
 *  Used by [[Arena]].
 *
 *  @param difficulty the [[Difficulty]] chosen by the user
 */
case class MapGenerator(difficulty: Difficulty.Value) {
  var currentLevel: Int = 0

  /** Generates a new level, populating the [[Arena]] with the resulting [[Entity]]s. */
  def generateLevel(): Unit = {
    currentLevel = currentLevel + 1
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = {
    val enemyNum: Int = between(difficulty.malusRange.min, difficulty.malusRange.max)
    val en: Set[EnemyCharacter] = Set.tabulate(enemyNum)(id => Enemy(randomPosition, id))
    arena.get.enemies = en
  }

  private def generateCollectibles(): Unit = {
    val bonusNum: Int = between(difficulty.bonusRange.min, difficulty.bonusRange.max)
    val collectibles: Set[Collectible] = Set.fill(bonusNum)(elem =
      if (Random.nextInt(1) == 0) BonusLife(randomPosition)
      else BonusScore(randomPosition, difficulty.bonusScore)
    )
    arena.get.collectibles = collectibles
  }

  private def generateObstacles(): Unit = {
    val obstaclesNum: Int = between(difficulty.malusRange.min, difficulty.malusRange.max)
    val obs: Set[Obstacle] = Set.fill(obstaclesNum)(elem = Obstacle(randomPosition))
    arena.get.obstacles = obs
  }

  private def levelMultiplier: Int = (arena.get.mapGen.currentLevel / difficulty.levelThreshold) + 1

  private def chance(prob: Double): Boolean = math.random < prob

  private def invProb(prob: Double): Double = 1 - prob
}

/** Utility methods for [[MapGenerator]]. */
object MapGenerator {
  /** Creates a [[MapGenerator]].
   *
   *  @param difficulty the [[Difficulty]] chosen by the user
   *  @return the new [[MapGenerator]] instance
   */
  def gameType(difficulty: DifficultyVal): MapGenerator = new MapGenerator(difficulty)

  /** Returns a random [[Position]] on the [[Arena]]. */
  @scala.annotation.tailrec
  def randomPosition: Position = {
    val x = between(1, arenaWidth - 1)
    val y = between(1, arenaHeight - 1)
    if (isClearFloor(x, y)) Position((x, y), Some(Down)) else randomPosition
  }
}