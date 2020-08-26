package gamelogic

import GameState.arena
import MovableEntity.stepPoint
import utilities.{Direction, Down, Left, Position, Right, Up}

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(var position: Position, var unexploded: Boolean = true) extends MovableEntity{

  def advances(): Unit = {
    position.dir.get match {
      case Up => move(Up)
      case Down => move(Down)
      case Left => move(Left)
      case Right => move(Right)
    }
  }

  override def move(dir: Direction): Unit = {
    if (canMove(stepPoint(position.point, dir))) {
      position = Position(stepPoint(position.point, dir), Some(dir))
    } else {
      unexploded = false
    }
  }

  override def remove(): Boolean = {
    unexploded = false
    true
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
