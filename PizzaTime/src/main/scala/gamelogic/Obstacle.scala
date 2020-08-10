package gamelogic

import utilities.Position

/** An obstacle placed on the [[Floor]].
 *
 *  @param position its [[Position]]
 */
case class Obstacle(var position: Position) extends Entity