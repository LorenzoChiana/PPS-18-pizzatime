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

  /** Generates a new level, populating the [[Arena]] with the resulting [[Entity]]s. */
  def generateLevel(): Unit = {
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = {
    val enemyNum: Int = between(difficulty.malusRange.min, difficulty.malusRange.max)
    //val en: Set[EnemyCharacter] = Set.tabulate(enemyNum)(id => Enemy(randomPosition))
    for(_ <- 0 to enemyNum){
      val e: EnemyCharacter = Enemy(randomPosition)
      arena.get.enemies += e
      arena.get.allGameEntities += e
    }
  }

  private def generateCollectibles(): Unit = {
    val bonusNum: Int = between(difficulty.bonusRange.min, difficulty.bonusRange.max)
   /* val collectibles: Set[Collectible] = Set.fill(bonusNum)(
      elem = if (Random.nextInt(2) == 0)
        BonusLife(randomPosition)
      else
        BonusScore(randomPosition, difficulty.bonusScore)
    ) */
    for(_ <- 0 to bonusNum){
      val bonus: Collectible = if(Random.nextInt(2) == 0) BonusLife(randomPosition) else BonusScore(randomPosition, difficulty.bonusScore)
      arena.get.collectibles += bonus
      arena.get.allGameEntities += bonus
    }
  }

  private def generateObstacles(): Unit = {
    val obstaclesNum: Int = between(difficulty.malusRange.min, difficulty.malusRange.max)
    val obstacleDim: Int = between(difficulty.obstacleDimension.min, difficulty.obstacleDimension.max)
    for (_ <- 0 to obstaclesNum) randomPositions(obstacleDim).foreach(p => arena.get.obstacles += Obstacle(p))

    arena.get.allGameEntities = arena.get.allGameEntities ++ arena.get.obstacles
  }

  private def levelMultiplier: Int = (GameState.level / difficulty.levelThreshold) + 1

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

  /** Returns between 1 and 4 random positions on the [[Arena]]. */
  @scala.annotation.tailrec
  def randomPositions(dim: Int): Set[Position] = {
    var obstacles: Set[Position] = Set()
    val x = between(1, arenaWidth - dim)
    val y = between(1, arenaHeight - 1)

    for (i <- 0 to dim)
      if (isClearFloor(x + i, y) && isClearFloor(x + i, y + 1) && isClearFloor(x + i, y - 1))
        obstacles = obstacles + Position((x + i, y), Some(Down))

    if (obstacles.size.equals(dim)) obstacles else randomPositions(dim)
  }
}