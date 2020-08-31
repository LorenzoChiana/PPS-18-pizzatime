package gamelogic

import utilities.{Down, Left, Point, Position, Right, Up}
import scala.util.Random.nextInt
import Arena._
import GameState.arena

/** An enemy character.
 *
 *  @param position its starting position
 *  @param lives its starting lives
 */
case class Enemy(var position: Position,  var lives: Int = 5, pointsKilling: Int = 20) extends EnemyCharacter {

  override def movementBehaviour(): Boolean = {
    nextInt(40) match {
      case 0 => move(Up)
      case 1 => move(Down)
      case 2 => move(Left)
      case 3 => move(Right)
      case _ => false
    }
  }

  override def canMove(p: Point): Boolean = super.canMove(p) && !containsCollectible(p) && !containsEnemy(p)

  def decreaseLife(): Unit = if (lives > 0) lives -= 1

  override def remove(): Boolean = {
    if (arena.get.enemies.contains(this)) {
      arena.get.enemies = arena.get.enemies - this
      true
    } else false
  }
}