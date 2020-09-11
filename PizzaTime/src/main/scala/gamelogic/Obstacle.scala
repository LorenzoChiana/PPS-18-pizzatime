package gamelogic

import utilities.{ObstacleImage, Position}
import GameState.arena
import utilities.Obstacle1Image.allObstacleImages

import scala.util.Random

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(var position: Position) extends Entity {
  val `type`: ObstacleImage = randomType

  override def remove(): Boolean = {
    arena.get.obstacles = arena.get.obstacles - copy()
    if (!arena.get.obstacles.contains(copy())) true else false
  }

  private def randomType: ObstacleImage = allObstacleImages(Random.nextInt(allObstacleImages.length))
}