package gamelogic

import MapGenerator._
import Arena._
import utilities.{Difficulty, Down, Point, Position}
import utilities.Difficulty._
import GameState._

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
    incLevel()
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def incLevel(): Unit = {
    currentLevel = currentLevel + 1
  }

  private def generateEnemies(): Unit = {
    val enemyNum: Int = between(difficulty.enemiesRange.min * levelMultiplier, difficulty.enemiesRange.max * levelMultiplier)

    for (_ <- 0 to enemyNum) {
      if (checkArenaPopulation()) {
        arena.get.enemies = arena.get.enemies + Enemy(randomClearPosition)
      }
    }
  }

  private def generateCollectibles(): Unit = {
    val bonusNum: Int = between(difficulty.collectiblesRange.min, difficulty.collectiblesRange.max)

    for (_ <- 0 to bonusNum) {
      if (checkArenaPopulation()) {
        val bonus: Collectible = if (nextInt(2) == 0) BonusLife(randomClearPosition) else BonusScore(randomClearPosition, difficulty.bonusScore)
        arena.get.collectibles = arena.get.collectibles + bonus
      }
    }
  }

  private def generateObstacles(): Unit = {
    val obstaclesNum: Int = between(difficulty.obstaclesRange.min, difficulty.obstaclesRange.max)
    val obstacleDim: Int = between(difficulty.obstacleDimension.min, difficulty.obstacleDimension.max)

    for (_ <- 0 to obstaclesNum) {
      if (checkArenaPopulation()) {
        val obstacles: Set[Obstacle] = randomAdjacentObstacles(obstacleDim)
        arena.get.obstacles = arena.get.obstacles ++ obstacles
      }
    }
  }

  def levelMultiplier: Int = (arena.get.mapGen.currentLevel / difficulty.levelThreshold) + 1

  private def randomAdjacentObstacles(dim: Int): Set[Obstacle] = {
    var startingObstacle: Obstacle = Obstacle(randomClearPosition)
    var obstacles: Set[Obstacle] = Set(startingObstacle)

    while (obstacles.size < dim) {
      startingObstacle.surroundings.foreach(p => {
        if (isClearFloor(p) && !isEntrance(p) && (obstacles.size < dim)) {
          obstacles = obstacles + Obstacle(Position(p, Some(Down)))
        }
      })
      if (obstacles.size < dim) {
        startingObstacle = Obstacle(randomClearPosition)
        obstacles = Set(startingObstacle)
      }
    }
    obstacles
  }
}

/** Utility methods for [[MapGenerator]]. */
object MapGenerator {
  /** Creates a [[MapGenerator]].
   *
   *  @param difficulty the [[Difficulty]] chosen by the user
   *  @return the new [[MapGenerator]] instance
   */
  def gameType(difficulty: DifficultyVal): MapGenerator = new MapGenerator(difficulty)

  /** Returns a random and clear [[Position]] on the wall. */
  @tailrec
  def randomPositionWall: Wall = {
    val wall = arena.get.walls.iterator.drop(between(0, arena.get.walls.size)).next

    wall.position.point match {
      case Point(0, 0) => randomPositionWall
      case Point(0, y) if y.equals(arenaHeight-1) => randomPositionWall
      case Point(x, 0) if x.equals(arenaWidth-1) => randomPositionWall
      case Point(x, y) if x.equals(arenaWidth-1) && y.equals(arenaHeight-1) => randomPositionWall
      case _ => if(wall.surroundings.size < 1) randomPositionWall else wall
    }
  }

  /** Returns a random and clear [[Position]] on the [[Arena]] (meaning that it's not occupied by any [[Entity]] and it's not on the entrance). */
  def randomClearPosition: Position = {
    val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.bullets ++ arena.get.collectibles ++ arena.get.obstacles
    val clearPoints: Set[Point] = arena.get.floor.map(_.position.point).diff(allEntities.map(_.position.point))
    var clearPoint: Point = clearPoints.toVector(nextInt(clearPoints.size))

    while (isEntrance(clearPoint)) {
      clearPoint = clearPoints.toVector(nextInt(clearPoints.size))
    }
    Position(clearPoint, Some(Down))
  }

}