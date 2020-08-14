package gamelogic

import MapGenerator._
import Arena._
import utilities.{Difficulty, Down, Position}
import utilities.Difficulty._
import GameState._
import utilities.ImplicitConversions._
import scala.util.Random.between

case class MapGenerator(bonusProb: Double, malusProb: Double) {
  var currentLevel: Int = 0

  def generateLevel(): Unit = {
    currentLevel += 1
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = {
    val range = (EnemiesRange + currentLevel) * levelMultiplier
    var en: Set[EnemyCharacter] = Set()
    for (_ <- 0 to between(1, range)) en = en + Enemy(randomPosition)
    arena.get.enemies = en
  }

  private def generateCollectibles(): Unit = {
    val range = CollectiblesRange + currentLevel
    var coll: Set[Collectible] = Set()
    for (_ <- 0 to between(1, range)) coll = coll + BonusScore(randomPosition)
    arena.get.collectibles = coll
  }

  private def generateObstacles(): Unit = {
    val range = ObstaclesRange + currentLevel
    var obs: Set[Obstacle] = Set()
    for (_ <- 0 to between(1, range)) obs = obs + Obstacle(randomPosition)
    arena.get.obstacles = obs
  }

  private def levelMultiplier: Int = currentLevel / LevelThreshold
}

object MapGenerator {
  val EnemiesRange: Int = 5
  val CollectiblesRange: Int = 2
  val ObstaclesRange: Int = 2
  val LevelThreshold: Int = 3

  def gameType(diff: Difficulty.Value): MapGenerator = diff match {
    case Easy => MapGenerator(0.9, 0.1)
    case Medium => MapGenerator(0.7, 0.3)
    case Hard => MapGenerator(0.3, 0.7)
    case Extreme => MapGenerator(0.1, 0.9)
  }

  @scala.annotation.tailrec
  def randomPosition: Position = {
    val x = between(1, arenaWidth)
    val y = between(1, arenaHeight)
    if (isClearFloor(x, y)) Position((x,y), Some(Down)) else randomPosition
  }

  def chance(prob: Double): Boolean = math.random < prob
}