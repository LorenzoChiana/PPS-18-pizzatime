package gamelogic

import MapGenerator._
import utilities.Difficulty
import utilities.Difficulty._

case class MapGenerator(bonusProb: Double, malusProb: Double) {
  var currentLevel: Int = 0

  def generateLevel(): Unit = {
    currentLevel += 1
    generateEnemies()
    generateCollectibles()
    generateObstacles()
  }

  private def generateEnemies(): Unit = ???

  private def generateCollectibles(): Unit = ???

  private def generateObstacles(): Unit = ???

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

  def chance(prob: Double): Boolean = math.random < prob
}