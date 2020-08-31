package gamelogic

import MovableEntity._
import utilities.{Direction, Down, Left, Position, Right, Up}
import GameState.arena

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(var position: Position, var unexploded: Boolean = true) extends MovableEntity {

  def advances(): Unit = {
    position.dir.get match {
      case Up => move(Up)
      case Down => move(Down)
      case Left => move(Left)
      case Right => move(Right)
    }
  }

  override def move(dir: Direction): Boolean = {
    if (canMove(stepPoint(position.point, dir))) {
      position = Position(stepPoint(position.point, dir), Some(dir))
      true
    } else {
      unexploded = false
      false
    }
  }

  override def remove(): Boolean = {
    unexploded = false
    arena.get.bullets = arena.get.bullets - copy()
    if (!arena.get.bullets.contains(copy())) true else false
  }
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
