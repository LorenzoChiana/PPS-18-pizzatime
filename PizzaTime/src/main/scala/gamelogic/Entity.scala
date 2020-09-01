package gamelogic

import utilities.{Down, Left, Point, Position, Right, Up}
import Arena._
import MovableEntity._
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
}
