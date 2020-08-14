package gamelogic

import utilities.ImplicitConversions._
import MovableEntity._
import Arena._
import utilities.{Direction, Up, Down, Left, Right, Point, Position}

/** An entity that can move.
 *  Implemented by [[Player]] and [[Bullet]].
 */
trait MovableEntity extends Entity {
  /** Returns the set of [[Point]] a [[MovableEntity]] can move to. */
  def surroundings: Set[Point] = {
    val surroundings: Set[Point] = Set(
      stepPoint(position.point, Up),
      stepPoint(position.point, Down),
      stepPoint(position.point, Left),
      stepPoint(position.point, Right)
    )
    surroundings -- bounds
  }
}

/** Utility methods for [[MovableEntity]]. */
object MovableEntity {
  /** Returns the adjacent [[Point]] in a given [[Direction]].
   *
   *  @param p the starting [[Point]]
   *  @param dir the [[Direction]] to consider
   */
  def stepPoint(p: Point, dir: Direction): Point = dir match {
    case Up => (p.x, p.y - 1)
    case Down => (p.x, p.y + 1)
    case Left => (p.x - 1, p.y)
    case Right => (p.x + 1, p.y)
  }
}
