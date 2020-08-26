package gamelogic

import GameState.arena
import MovableEntity.stepPoint
import utilities.{Direction, Down, Left, Position, Right, Up}

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
case class Bullet(var position: Position, var unexplode: Boolean = true) extends MovableEntity{

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
      unexplode = false
      false
    }
  }

  override def remove(): Boolean = {
      unexplode = false
      true
  }
}
