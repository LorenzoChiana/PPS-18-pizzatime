package gamelogic

import utilities.Position

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(var position: Position) extends MovableEntity