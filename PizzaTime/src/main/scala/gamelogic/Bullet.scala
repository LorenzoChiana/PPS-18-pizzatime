package gamelogic

import gamelogic.GameState.arena
import gamelogic.MovableEntity.stepPoint
import utilities.{Direction, Down, Left, Position, Right, Up}

/** A bullet fired by the [[Player]].
 *
 *  @param position its initial [[Position]]
 */
class Bullet(var position: Position) extends MovableEntity{

  def advances(): Unit = {
    position.dir.get match {
      case Up => move(Up)
      case Down => move(Down)
      case Left => move(Left)
      case Right => move(Right)
    }
  }

  override def move(dir: Direction): Unit = {
    if (canMove(stepPoint(position.point, dir)))
      position = Position(stepPoint(position.point, dir), Some(dir))
    else
      arena.get.bullets = arena.get.bullets - this
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