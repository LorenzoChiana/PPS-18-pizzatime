package gamelogic

import utilities.{Down, Left, Position, Right, Up}

import scala.util.Random.nextInt
import utilities.Position.changePosition

/** An enemy character.
 *
 *  @param position its starting position
 *  @param lives its starting lives
 */
case class Enemy(position: Position, lives: Int) extends EnemyCharacter {
  val pointsKilling = 20

  private var disableBehavior = false

  override def movementBehaviour: Option[EnemyCharacter] =
    if (!disableBehavior)
      nextInt(40) match {
        case 0 => Some(Enemy(changePosition(position, Up), lives))
        case 1 => Some(Enemy(changePosition(position, Down), lives))
        case 2 => Some(Enemy(changePosition(position, Left), lives))
        case 3 => Some(Enemy(changePosition(position, Right), lives))
        case _ => None
      }
    else None

  def onTestingMode(): Unit = disableBehavior = true

}

object Enemy {
  val maxLife = 5

  def apply(p: Position): Enemy = Enemy(p, maxLife)

}