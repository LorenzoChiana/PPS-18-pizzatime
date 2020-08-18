package gamelogic

import utilities.{Down, Position, Up, Left, Right}

/** An enemy character.
 *
 *  @param position its starting position
 *  @param id a unique number for reference
 */
class Enemy(var position: Position, var id: Int) extends EnemyCharacter {
  var lives: Int = 5

  val dir = scala.util.Random

  override def movementBehaviour(): Unit = {
    val dir = scala.util.Random
    dir.nextInt(4) match {
      case 0 => move(Up)
      case 1 => move(Down)
      case 2 => move(Left)
      case 3 => move(Right)
    }
  }

}

object Enemy {
  def apply(position: Position, id: Int): Enemy = new Enemy(position, id)
}