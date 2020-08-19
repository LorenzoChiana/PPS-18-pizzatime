package gamelogic

import utilities.{Down, Left, Position, Right, Up, Point}
import scala.util.Random.nextInt
import Arena._

/** An enemy character.
 *
 *  @param position its starting position
 *  @param id a unique number for reference
 */
case class Enemy(var position: Position, id: Int) extends EnemyCharacter {
  var lives: Int = 5

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