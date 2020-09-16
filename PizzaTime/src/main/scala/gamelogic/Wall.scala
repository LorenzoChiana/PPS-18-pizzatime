package gamelogic

import utilities.{Point, Position}
import  Arena.isClearFloor

/** The [[GameMap]]'s wall, situated on its bounds.
 *
 *  @param position its [[Position]]
 */
case class Wall(position: Position) extends Entity {

  /** Returns the set of [[Point]]s representing the [[Wall]]'s surroundings without other [[Entity]]s. */
  override def surroundings(horizontal: Boolean = true, vertical: Boolean = true): Set[Point] =
    super.surroundings().filter(isClearFloor)
}