package gamelogic

import utilities.{Down, Left, Point, Position, Right, Up}
import Arena.checkBounds
import utilities.Position.changePosition

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Door]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  val position: Position

  /** Returns the set of [[Point]]s that represent an [[Entity]]'s surroundings. */
  def surroundings(horizontal: Boolean = true, vertical: Boolean = true): Set[Point] = {
    var surroundings: Set[Point] = Set()

    if (horizontal) {
      surroundings = surroundings ++ Set(changePosition(position, Left).point, changePosition(position, Right).point)
    }
    if (vertical) {
      surroundings = surroundings ++ Set(changePosition(position, Up).point, changePosition(position, Down).point)
    }
    surroundings.filter(checkBounds(_))
  }

}

/** Utility methods for [[Entity]]. */
object Entity {

 /* def nearPoint(p: Point, dir: Direction): Point = dir match {
    case Up => (p.x, p.y - 1)
    case Down => (p.x, p.y + 1)
    case Left => (p.x - 1, p.y)
    case Right => (p.x + 1, p.y)
  } */
}
