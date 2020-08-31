package gamelogic

import utilities.Position
import GameState.arena

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(var position: Position) extends Entity {
  override def remove(): Boolean = {
    arena.get.obstacles = arena.get.obstacles - copy()
    if (!arena.get.obstacles.contains(copy())) true else false
  }
}