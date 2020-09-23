package gamelogic

import utilities.IdGenerator.nextId
import utilities.Position.changePosition
import utilities.Position

/** A bullet fired by the [[Hero]].
 *
 *  @param position its [[Position]]
 */
case class Bullet(id: Int, position: Position, unexploded: Boolean, bulletRange: Int) extends MovableEntity {

  def move(): Bullet = Bullet(id, changePosition(position, position.dir.get), checkInRange(bulletRange), incRange(bulletRange))

  private def incRange(range: Int): Int = range + 1

  private def checkInRange(range: Int): Boolean = range < Bullet.range
}

object Bullet {
  val range: Int = 5

  /** Creates a [[Bullet]] with a given [[Position]].
   *
   *  @param position its initial [[Position]]
   *  @return the new [[Bullet]] instance
   */
  def apply(position: Position): Bullet = Bullet(nextId, position, unexploded = true, 0)
}


