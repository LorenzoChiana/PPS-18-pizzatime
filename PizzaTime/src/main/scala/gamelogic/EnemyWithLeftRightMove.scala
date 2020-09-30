package gamelogic

import alice.tuprolog.Term
import utilities.IdGenerator.nextId
import utilities.{Position, Scala2P, Right, Left}

/** An enemy character.
 *
 *  @param position its position
 *  @param lives its lives
 */
case class EnemyWithLeftRightMove(id: Int, position: Position, lives: Int) extends LivingEntity with EnemyCharacter {
  val pointsKilling: Int = 20
  private var disableBehavior: Boolean = false

  import Scala2P._

  override def movementBehaviour: Option[EnemyCharacter] = {
    var result: Seq[Option[Position]] = Seq()

    position.dir match {
      case Some(Right) | None => result = engineForLeftRightMove("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", right, Point, Dir) ")
      case Some(Left) => result = engineForLeftRightMove("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", left, Point, Dir) ")
    }
    println(result.head.get)
    result.size match {
      case 0 => Some(EnemyWithLeftRightMove(id, position, lives))
      case _ => Some(EnemyWithLeftRightMove(id, result.head.get,lives))
    }
  }

  def onTestingMode(): Unit = {
    disableBehavior = true
  }
}

object EnemyWithLeftRightMove {
  val maxLife: Int = 5

  def apply(p: Position): EnemyWithLeftRightMove = EnemyWithLeftRightMove(nextId, p, maxLife)
}