package gamelogic

import utilities.{Direction, Down, Left, Point, Position, Right, Up}
import Entity._
import Arena.checkBounds
import utilities.ImplicitConversions._

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Door]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  var position: Position

  /** Returns the set of [[Point]]s that represent an [[Entity]]'s surroundings. */
  def surroundings(horizontal: Boolean = true, vertical: Boolean = true): Set[Point] = {
    var surroundings: Set[Point] = Set()

    if (horizontal) {
      surroundings = surroundings ++ Set(nearPoint(position.point, Left), nearPoint(position.point, Right))
    }
    if (vertical) {
      surroundings = surroundings ++ Set(nearPoint(position.point, Up), nearPoint(position.point, Down))
    }
    surroundings.filter(checkBounds(_))
  }

  /** Removes the [[Entity]] from the [[Arena]].
   *
   *  @return true if the [[Entity]] is present in the [[Arena]] and can be removed
   */
  def remove(): Boolean = false
}

/** Utility methods for [[Entity]]. */
object Entity {
  /** Returns the adjacent [[Point]] in a given [[Direction]].
   *
   *  @param p the starting [[Point]]
   *  @param dir the [[Direction]] to consider
   */
  def nearPoint(p: Point, dir: Direction): Point = dir match {
    case Up => (p.x, p.y - 1)
    case Down => (p.x, p.y + 1)
    case Left => (p.x - 1, p.y)
    case Right => (p.x + 1, p.y)
  }
}
