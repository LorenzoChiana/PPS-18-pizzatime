package gamelogic

import Arena._
import utilities.{Direction, Down, Left, Point, Position, Right, Up}

/** An entity that can move.
 *  Implemented by [[Hero]], [[Bullet]] and [[EnemyCharacter]].
 */
trait MovableEntity extends Entity

object MovableEntity{
  /** Changes the [[Direction]] of the [[MovableEntity]].
   *
   *  @param e [[MovableEntity]] to change [[Direction]]
   *  @param dir the new [[Direction]]
   *
   *  @return new [[MovableEntity]] with new [[Direction]]
   */
  def changeDirection(e: MovableEntity, dir: Direction): MovableEntity = e match {
    case e: Enemy => Enemy(Position.changeDirection(e.position, dir), e.lives)
    case e: Hero => Hero(Position.changeDirection(e.position, dir), e.lives)
    case _ => e
  }

  /** Moves the [[MovableEntity]] one step forward in a given [[Direction]].
   *
   *  @param dir the [[Direction]] of movement
   *  @return true if the movement occurred
   */
  def move(e: MovableEntity, dir: Direction): MovableEntity = e match {
    case e: Enemy => Enemy(Position.changePosition(e.position, dir), e.lives)
    case h: Hero => Hero(Position.changePosition(e.position, dir), h.lives)
    case _ => e
  }

  /** Moves the [[MovableEntity]] to the specified [[Position]].
   *
   *  @param pos the destination's [[Position]]
   *  @return true if the movement occurred
   */
  def moveTo(e: MovableEntity, pos: Position): MovableEntity = e match {
    case e: Enemy => Enemy(pos, e.lives)
    case h: Hero => Hero(pos, h.lives)
    case _ => e
  }

  /** Changes the [[Direction]] of the [[MovableEntity]] and moves it one step forward in the same [[Direction]]
   *
   * @param dir the [[Direction]] of movement
   */
  def changeDirectionAndMove(e: MovableEntity, dir: Direction): MovableEntity = {
    val entity = changeDirection(e, dir)
    move(entity, dir)
  }

  /** Returns true if a [[Point]] is transitable.
   *
   *  @param p the [[Point]] to check
   */
  def canMoveIn(e: MovableEntity, p: Point): Boolean = e.surroundings().contains(p) && !containsObstacle(p)

  /** Check if an entity can move
   *
   * @return true if there is a [[Point]] transitable
   */
  def canMove(e: MovableEntity): Boolean = List(Up, Down, Right, Left).forall(direction => canMoveIn(e, Position.changePosition(e.position, direction).point))

}

/* def changeDirection(e: MovableEntity, dir: Direction): MovableEntity = e match {
    case e: EnemyCharacter => Enemy(e.position.changeDirection(dir), e.lives)
    case e: Hero => Hero( e.position.changeDirection(dir))
    case _ => e
  }*/
