package gamelogic

import utilities.{Direction, Position}

/** An entity that can move.
 *  Implemented by [[Hero]], [[Bullet]] and [[EnemyCharacter]].
 */
trait MovableEntity extends Entity {

  /** Changes the [[Direction]] of the [[MovableEntity]].
   *
   *  @param dir the new [[Direction]]
   *  @return new [[MovableEntity]] with new [[Direction]]
   */
  def changeDirection(dir: Direction): MovableEntity = this match {
    case e: EnemyCharacter => Enemy(e.id, Position.changeDirection(e.position, dir), e.lives)
    case h: Hero => Hero(Position.changeDirection(h.position, dir), h.lives)
    case _ => this
  }

  /** Moves the [[MovableEntity]] one step forward in a given [[Direction]].
   *
   *  @param dir the [[Direction]] of movement
   *  @return true if the movement occurred
   */
  def move(dir: Direction): MovableEntity = this match {
      case e: EnemyCharacter => Enemy(e.id, Position.changePosition(e.position, dir), e.lives)
      case h: Hero => Hero(Position.changePosition(h.position, dir), h.lives)
      case _ => this
  }

  /** Moves the [[MovableEntity]] to the specified [[Position]].
   *
   *  @param pos the destination's [[Position]]
   *  @return true if the movement occurred
   */
  def moveTo(pos: Position): MovableEntity = this match {
    case e: EnemyCharacter => Enemy(e.id, pos, e.lives)
    case h: Hero => Hero(pos, h.lives)
    case _ => this
  }
}

