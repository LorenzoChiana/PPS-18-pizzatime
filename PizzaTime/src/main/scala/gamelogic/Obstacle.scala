package gamelogic

import utilities.Position
import gamelogic.Sink.allObstacleTypes
import scala.util.Random.nextInt

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */

case class Obstacle(position: Position, `type`: ObstacleType) extends Entity

object Obstacle{

  def apply(pos: Position): Obstacle = Obstacle(pos, randomType)

  private def randomType: ObstacleType = allObstacleTypes(nextInt(allObstacleTypes.length))

}