package gamelogic

import utilities.Position

/** The main character.
 *
 *  @param playerName its name
 *  @param position its starting position
 */
case class Player(playerName: String, var position: Position) extends MovableEntity