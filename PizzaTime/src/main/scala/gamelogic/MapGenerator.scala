package gamelogic

import MapGenerator._
import Arena._
import utilities.{Difficulty, Down, Position}
import utilities.Difficulty._
import GameState._
import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.ImplicitConversions._

import scala.util.Random.between

/** Encapsulates the logic for generating a new level.
 *  Used by [[Arena]].
 *
 *  @param bonusProb the bonus probability
 *  @param malusProb the malus probability
 */
case class MapGenerator(bonusProb: Double, malusProb: Double) {
  var currentLevel: Int = 0

  /** Generates a new level, populating the [[Arena]] with the resulting [[Entity]]s. */
  def generateLevel(): Unit = {
    currentLevel += 1
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = {
    val range = (EnemiesRange + currentLevel) //* levelMultiplier
    var en: Set[EnemyCharacter] = Set()
    var id: Int = 0
    for (_ <- 0 to between(1, range)) { en = en + Enemy(randomPosition, id); id = id+1 }
    arena.get.enemies = en
  }

  private def generateCollectibles(): Unit = {
    for (_ <- 0 to between(difficulty.bonusRange.min, difficulty.bonusRange.max)) arena.get.collectibles = arena.get.collectibles + BonusLife(randomPosition)
    for (_ <- 0 to between(difficulty.bonusRange.min, difficulty.bonusRange.max)) arena.get.collectibles = arena.get.collectibles + BonusScore(randomPosition, difficulty.bonusScore)
  }

  private def generateObstacles(): Unit = {
    val range = ObstaclesRange + currentLevel
    var obs: Set[Obstacle] = Set()
    for (_ <- 0 to between(1, range)) obs = obs + Obstacle(randomPosition)
    arena.get.obstacles = obs
  }

  private def levelMultiplier: Int = currentLevel / LevelThreshold
}

/** Utility methods for [[MapGenerator]]. */
object MapGenerator {
  val EnemiesRange: Int = 5
  val CollectiblesRange: Int = 2
  val ObstaclesRange: Int = 2
  val LevelThreshold: Int = 3

  /** Creates a [[MapGenerator]].
   *
   *  @param diff the difficulty value to use
   *  @return the new [[MapGenerator]] instance
   */
  def gameType(diff: Difficulty.Value): MapGenerator = diff match {
    case Easy => MapGenerator(0.9, 0.1)
    case Medium => MapGenerator(0.7, 0.3)
    case Hard => MapGenerator(0.3, 0.7)
    case Extreme => MapGenerator(0.1, 0.9)
  }

  /** Returns a random position on the [[Arena]]. */
  @scala.annotation.tailrec
  def randomPosition: Position = {
    val x = between(1, arenaWidth - 1)
    val y = between(1, arenaHeight - 1)
    if (isClearFloor(x, y)) Position((x,y), Some(Down)) else randomPosition
  }

  def chance(prob: Double): Boolean = math.random < prob
}