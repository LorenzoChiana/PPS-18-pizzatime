package gamelogic

import Arena._
import utilities.{Difficulty, Down, IdGenerator, Point, Position}
import IdGenerator.nextId
import utilities.Difficulty._
import GameState._
import Door._
import utilities.ImplicitConversions._

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
    initializeDoor()
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  def levelMultiplier: Int = (currentLevel / difficulty.levelThreshold) + 1

  private def incLevel(): Unit = {
    currentLevel += 1
  }

  private def initializeDoor(): Unit = {
    if (arena.get.door.isEmpty) {
      arena.get.door = Some(exitDoor(arena.get.walls))
    }
    arena.get.walls = arena.get.walls.filter(!_.position.point.equals(arena.get.door.get.position.point))
  }

  private def generateEnemies(): Unit = {
    val enemyNum: Int = between(difficulty.enemiesRange.min * levelMultiplier, difficulty.enemiesRange.max * levelMultiplier)

    for (_ <- 0 to enemyNum) {
      if (checkArenaPopulation()) {
        val enemy: EnemyCharacter = if(nextInt(2) == 0) EnemyWithLeftRightMove(randomClearPosition) else EnemyWithRandomMove(randomClearPosition)
        arena.get.enemies = arena.get.enemies + enemy
      }
    }
  }

  private def generateCollectibles(): Unit = {
    val bonusNum: Int = between(difficulty.collectiblesRange.min, difficulty.collectiblesRange.max)

    for (_ <- 0 to bonusNum) {
      if (checkArenaPopulation()) {
        val bonus: Collectible = if (nextInt(2) == 0) BonusLife(nextId, randomClearPosition) else BonusScore(nextId, randomClearPosition, difficulty.bonusScore)
        arena.get.collectibles = arena.get.collectibles + bonus
      }
    }
  }

  private def generateObstacles(): Unit = {
    val obstaclesNum: Int = between(difficulty.obstaclesRange.min, difficulty.obstaclesRange.max)

    for (_ <- 0 to obstaclesNum) {
      if (checkArenaPopulation()) {
        val obstacleDim: Int = between(difficulty.obstacleDimension.min, difficulty.obstacleDimension.max)
        val obstacles: Set[Obstacle] = randomAdjacentObstacles(obstacleDim)
        arena.get.obstacles = arena.get.obstacles ++ obstacles
      }
    }
    removeFloorBorderObstacles()
    removeVerticalObstacles()
  }

  private def randomAdjacentObstacles(dim: Int): Set[Obstacle] = {
    var startingObstacle: Obstacle = Obstacle(randomClearPosition)
    var obstacles: Set[Obstacle] = Set(startingObstacle)

    while (obstacles.size < dim) {
      surroundings(startingObstacle).foreach(p => {
        if (isClearFloor(p) && (obstacles.size < dim)) {
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

  private def removeFloorBorderObstacles(): Unit = {
    var floorBorders: Set[Point] = Set()

    for (
      x <- 0 until arenaWidth;
      y <- 0 until arenaHeight
    ) floorBorders = floorBorders ++ Set[Point](
      (x, 1),
      (1, y),
      (x, arenaHeight - 2),
      (arenaWidth - 2, y)
    )
    arena.get.obstacles.filter(obstacle => floorBorders.contains(obstacle.position.point)).foreach(e => arena.get.removeEntity(e))
  }

  private def removeVerticalObstacles(): Unit = {
    arena.get.obstacles.filter(obstacle => surroundings(obstacle, horizontal = false).exists(containsObstacle)).foreach(e => arena.get.removeEntity(e))
  }

  private def randomClearPosition: Position = {
    val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.bullets ++ arena.get.collectibles ++ arena.get.obstacles
    val clearPoints: Set[Point] = arena.get.floor.map(_.position.point).diff(allEntities.map(_.position.point))
    var clearPoint: Point = clearPoints.toVector(nextInt(clearPoints.size))

    while (isEntrance(clearPoint)) {
      clearPoint = clearPoints.toVector(nextInt(clearPoints.size))
    }
    Position(clearPoint, None)
  }
}
