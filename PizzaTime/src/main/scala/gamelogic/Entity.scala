package gamelogic

import utilities.{Direction, Down, Left, Point, Position, Right, Up}
import Arena._
import utilities.ImplicitConversions._

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  var position: Position

  /** Removes the [[Entity]] from the [[Arena]].
   *
   *  @return true if the [[Entity]] is present in the [[Arena]] and can be removed, false otherwise
   */
  def remove(): Boolean = false
}

/** Utility methods for [[Entity]]. */
object Entity {
  /** Returns the set of [[Point]]s defining the [[Entity]]'s surroundings.
   *
   *  @param p the starting [[Point]]
   */
  def surroundings(p: Point): Set[Point] = {
    val surroundings: Set[Point] = Set(
      stepPoint(p, Up),
      stepPoint(p, Down),
      stepPoint(p, Left),
      stepPoint(p, Right),
      (p.x - 1, p.y + 1),
      (p.x + 1, p.y + 1),
      (p.x + 1, p.y - 1),
      (p.x - 1, p.y - 1)
    )
    surroundings -- bounds
  }

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
