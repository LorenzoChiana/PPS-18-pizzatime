package gamelogic

import utilities.{ObstacleImage, Position}
import utilities.Obstacle1Image.allObstacleImages

import scala.util.Random

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(position: Position, `type`: ObstacleImage) extends Entity

object Obstacle{
  private def randomType: ObstacleImage = allObstacleImages(Random.nextInt(allObstacleImages.length))

  def apply(pos: Position): Obstacle = Obstacle(pos, randomType)
}