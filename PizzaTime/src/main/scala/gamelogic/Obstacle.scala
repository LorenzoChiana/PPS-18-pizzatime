package gamelogic

import utilities.Position
import GameState.arena

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(var position: Position) extends Entity {
  override def remove(): Boolean = {
    if (arena.get.obstacles.contains(this)) {
      arena.get.obstacles = arena.get.obstacles - this
      true
    } else false
  }
}