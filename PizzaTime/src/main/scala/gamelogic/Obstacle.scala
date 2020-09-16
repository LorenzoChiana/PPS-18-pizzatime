package gamelogic

import utilities.Position
import GameState.arena
import gamelogic.Sink.allObstacleTypes
import scala.util.Random.nextInt

import scala.util.Random

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(var position: Position) extends Entity {
    val `type`: ObstacleType = randomType

    override def remove(): Boolean = {
      arena.get.obstacles = arena.get.obstacles - copy()
      !arena.get.obstacles.contains(copy())
    }

    private def randomType: ObstacleType = allObstacleTypes(nextInt(allObstacleTypes.length))
}