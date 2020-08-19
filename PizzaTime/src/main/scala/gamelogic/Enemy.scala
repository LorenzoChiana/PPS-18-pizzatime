package gamelogic

import utilities.{Down, Left, Position, Right, Up, Point}
import scala.util.Random.nextInt
import Arena._

/** An enemy character.
 *
 *  @param position its starting position
 *  @param lives its starting lives
 */
case class Enemy(var position: Position,  var lives: Int = 5) extends EnemyCharacter {

  def movementBehaviour(): Unit = {
    nextInt(4) match {
      case 0 => move(Up)
      case 1 => move(Down)
      case 2 => move(Left)
      case 3 => move(Right)
    }
  }

  override def canMove(p: Point): Boolean = super.canMove(p) && !containsCollectible(p) && !containsEnemy(p)
}