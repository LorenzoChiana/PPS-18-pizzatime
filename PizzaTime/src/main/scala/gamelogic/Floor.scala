package gamelogic

import utilities.Position

/** The [[GameMap]]'s floor, inside the [[Wall]]s.
 *
 *  @param position its [[Position]]
 */
case class Floor(var position: Position) extends Entity