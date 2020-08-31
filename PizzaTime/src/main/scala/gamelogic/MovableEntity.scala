package gamelogic

import utilities.ImplicitConversions._
import MovableEntity._
import Arena._
import utilities.{Direction, Up, Down, Left, Right, Point, Position}

/** An entity that can move.
 *  Implemented by [[Player]], [[Bullet]] and [[EnemyCharacter]].
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

  /** Returns true if a [[Point]] is transitable.
   *
   *  @param p the [[Point]] to check
   */
  def canMove(p: Point): Boolean = surroundings.contains(p) && !containsObstacle(p)

  /** Moves the [[MovableEntity]] one step forward in a given [[Direction]].
   *
   *  @param dir the [[Direction]] of movement
   */
  def move(dir: Direction): Boolean = {
    if (dir.equals(position.dir.get) && canMove(stepPoint(position.point, dir))) {
      position = Position(stepPoint(position.point, dir), Some(dir)); true
    } else {
      changeDirection(dir); false
    }
  }

  /** Changes the [[Direction]] of the [[MovableEntity]].
   *
   *  @param dir the new [[Direction]]
   */
  def changeDirection(dir: Direction): Unit = {
    position = Position(position.point, Some(dir))
  }

  /** Moves the [[MovableEntity]] to the specified [[Position]].
   *
   *  @param pos the destination's [[Position]]
   */
  def moveTo(pos: Position): Unit = {
    if (!containsObstacle(pos.point)) {
      position = pos
    }
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
