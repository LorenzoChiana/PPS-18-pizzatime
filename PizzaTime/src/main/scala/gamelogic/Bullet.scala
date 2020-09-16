package gamelogic

import utilities.Position.changePosition
import utilities.{Down, Left, Position, Right, Up}

/** A bullet fired by the [[Hero]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(position: Position, unexploded: Boolean, bulletRange: Int ) extends MovableEntity {

  /*canMoveIn(nearPoint(position.point, dir)) && checkInRange)
  override def canMove: Boolean = canMoveIn(position.changePosition(position.dir.get).point)
*/

}

object Bullet {
  val range: Int = 5

  /** Creates a [[Bullet]] with a given [[Position]].
   *
   *  @param position its initial [[Position]]
   *  @return the new [[Bullet]] instance
   */
  def apply(position: Position): Bullet = Bullet(position, unexploded = true, 0)

  def move(b: Bullet): Bullet = {
    b.position.dir.get match {
      case Up => Bullet(changePosition(b.position ,Up), checkInRange(b.bulletRange), incRange(b.bulletRange))
      case Down => Bullet(changePosition(b.position, Down), checkInRange(b.bulletRange), incRange(b.bulletRange))
      case Left => Bullet(changePosition(b.position, Left), checkInRange(b.bulletRange), incRange(b.bulletRange))
      case Right => Bullet(changePosition(b.position, Right), checkInRange(b.bulletRange), incRange(b.bulletRange))
    }
  }

  private def incRange(range: Int): Int = range + 1

  private def checkInRange(range: Int): Boolean = range < Bullet.range

}


