package gamelogic

import gamelogic.Arena.{containsCollectible, containsEnemy}
import utilities.{Down, Left, Point, Position, Right, Up}

import scala.util.Random

/** An enemy character.
 *
 *  @param position its starting position
 *  @param id a unique number for reference
 */
class Enemy(var position: Position, var id: Int) extends EnemyCharacter {
  var lives: Int = 5

  val dir: Random.type = scala.util.Random

  override def movementBehaviour(): Unit = {
    val dir = scala.util.Random
    dir.nextInt(4) match {
      case 0 => move(Up)
      case 1 => move(Down)
      case 2 => move(Left)
      case 3 => move(Right)
    }
  }

  override def canMove(p: Point): Boolean = super.canMove(p) && !containsCollectible(p) && !containsEnemy(p)

}

object Enemy {
  def apply(position: Position, id: Int): Enemy = new Enemy(position, id)
}