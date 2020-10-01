package gamelogic

import utilities.IdGenerator.nextId
import utilities.{Position, Scala2P}

/** An enemy character.
 *
 *  @param position its position
 *  @param lives its lives
 */
case class EnemyWithRandomMove(id: Int, position: Position, lives: Int) extends LivingEntity with EnemyCharacter {
  val pointsKilling: Int = 20
  private var disableBehavior: Boolean = false

  import Scala2P._

  override def movementBehaviour: Option[EnemyCharacter] = {
    val result = engineForRandomMove("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", Point) " )
    result.size match {
      case 0 => Some(EnemyWithRandomMove(id, position, lives))
      case _ => Some(EnemyWithRandomMove(id, Position(result.head.get,position.dir),lives))
    }
  }

  def onTestingMode(): Unit = {
    disableBehavior = true
  }
}

object EnemyWithRandomMove {
  val maxLife: Int = 5

  def apply(p: Position): EnemyWithRandomMove = EnemyWithRandomMove(nextId, p, maxLife)
}