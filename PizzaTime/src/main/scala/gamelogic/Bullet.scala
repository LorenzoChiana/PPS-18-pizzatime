package gamelogic

import Entity._
import utilities.{Direction, Down, Left, Position, Right, Up}
import GameState.arena

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(var position: Position, var unexploded: Boolean = true, range: Int = 5) extends MovableEntity {
  private var bulletRange: Int = 0

  def advances(): Unit = {
    position.dir.get match {
      case Up => move(Up)
      case Down => move(Down)
      case Left => move(Left)
      case Right => move(Right)
    }
  }

  override def move(dir: Direction): Boolean = {
    incRange()
    if (canMoveIn(nearPoint(position.point, dir)) && checkInRange) {
      position = Position(nearPoint(position.point, dir), Some(dir))
      true
    } else {
      unexploded = false
      false
    }
  }

  override def canMove: Boolean = canMoveIn(nearPoint(position.point, position.dir.get))

  override def remove(): Boolean = {
    unexploded = false
    arena.get.bullets = arena.get.bullets - copy()
    !arena.get.bullets.contains(copy())
  }

  private def incRange(): Unit = bulletRange += 1

  private def checkInRange: Boolean = bulletRange < range
}

/** Factory for [[Bullet]] instances. */
object Bullet {
  /** Creates a [[Bullet]] with a given [[Position]].
   *
   *  @param position its initial [[Position]]
   *  @return the new [[Bullet]] instance
   */
  def apply(position: Position): Bullet = new Bullet(position)
}
