package gamelogic

import utilities.Position

/** The main character.
 *
 *  @param position its starting position
 */
case class Player(var position: Position) extends MovableEntity