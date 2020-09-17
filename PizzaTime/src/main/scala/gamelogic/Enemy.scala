package gamelogic

import utilities.IdGenerator.nextId
import utilities.{Down, IdGenerator, Left, Position, Right, Up}

import scala.util.Random.nextInt
import utilities.Position.changePosition

/** An enemy character.
 *
 *  @param position its position
 *  @param lives its lives
 */
case class Enemy(id: Int, position: Position, lives: Int) extends LivingEntity with EnemyCharacter {
  val pointsKilling: Int = 20

  private var disableBehavior: Boolean = false

  override def movementBehaviour: Option[EnemyCharacter] =
    if (!disableBehavior)
      nextInt(40) match {
        case 0 => Some(Enemy(id, changePosition(position, Up), lives))
        case 1 => Some(Enemy(id, changePosition(position, Down), lives))
        case 2 => Some(Enemy(id, changePosition(position, Left), lives))
        case 3 => Some(Enemy(id, changePosition(position, Right), lives))
        case _ => None
      }
    else None

  def onTestingMode(): Unit = disableBehavior = true

}

object Enemy {
  val maxLife: Int = 5

  def apply(p: Position): Enemy = Enemy(nextId, p, maxLife)
}