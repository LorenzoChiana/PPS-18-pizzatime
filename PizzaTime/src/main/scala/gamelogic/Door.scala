package gamelogic

import utilities.Position

/** The [[GameMap]]'s door, used to move the [[Player]] between the levels.
 *
 *  @param position its [[Position]]
 */
case class Door(var position: Position) extends Entity