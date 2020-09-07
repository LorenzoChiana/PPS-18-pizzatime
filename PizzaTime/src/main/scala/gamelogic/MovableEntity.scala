package gamelogic

import utilities.ImplicitConversions._
import MovableEntity._
import Arena._
import utilities.{Direction, Down, Left, Point, Position, Right, Up}

/** An entity that can move.
 *  Implemented by [[Player]], [[Bullet]] and [[EnemyCharacter]].
 */
trait MovableEntity extends Entity {
  /** Returns the set of [[Point]]s a [[MovableEntity]] can move to. */
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
  def canMoveIn(p: Point): Boolean = surroundings.contains(p) && !containsObstacle(p)

  /** Check if an entity can move
   *
   * @return true if there is a [[Point]] transitable
   */
  def canMove: Boolean = List(Up, Down, Right, Left).forall(direction => canMoveIn(stepPoint(position.point, direction)))

  /** Moves the [[MovableEntity]] one step forward in a given [[Direction]].
   *
   *  @param dir the [[Direction]] of movement
   *  @return true if the movement occurred, false otherwise
   */
  def move(dir: Direction): Boolean = {
    if (dir.equals(position.dir.get) && canMoveIn(stepPoint(position.point, dir))) {
      position = Position(stepPoint(position.point, dir), Some(dir))
      true
    } else {
      changeDirection(dir)
      false
    }
  }

  /** Changes the [[Direction]] of the [[MovableEntity]].
   *
   *  @param dir the new [[Direction]]
   */
  def changeDirection(dir: Direction): Unit = {
    position = Position(position.point, Some(dir))
  }

  /** Changes the [[Direction]] of the [[MovableEntity]] and moves it one step forward in the same [[Direction]]
   *
   * @param dir the [[Direction]] of movement
   */
  def changeDirectionAndMove(dir: Direction): Unit = {
    changeDirection(dir)
    move(dir)
  }

  /** Moves the [[MovableEntity]] to the specified [[Position]].
   *
   *  @param pos the destination's [[Position]]
   *  @return true if the movement occurred, false otherwise
   */
  def moveTo(pos: Position): Boolean = {
    if (!containsObstacle(pos.point)) {
      position = pos
      true
    } else false
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
