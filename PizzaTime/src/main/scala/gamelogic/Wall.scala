package gamelogic

import utilities.{Point, Position}

/** The [[GameMap]]'s wall, situated on its bounds.
 *
 *  @param position its [[Position]]
 */
case class Wall(var position: Position) extends Entity{

  /**
   * Returns the set of [[Point]]s defining the [[Wall]]'s surroundings without obstacle .
   */
  override def surroundings: Set[Point] = {
  super.surroundings
    .filter(p => Arena.isClearFloor(p))
  }
}