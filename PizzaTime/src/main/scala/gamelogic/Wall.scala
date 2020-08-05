package gamelogic

import utilities.Position

/** The [[GameMap]]'s wall, situated on its bounds.
 *
 *  @param position its [[Position]]
 */
case class Wall(var position: Position) extends Entity