package gamelogic

import alice.tuprolog.Term
import utilities.IdGenerator.nextId
import utilities.{Down, Left, Point, Position, Right, Scala2P, Up}

import scala.util.Random.nextInt
import utilities.Position.changePosition
import utilities.Scala2P.prolog

/** An enemy character.
 *
 *  @param position its position
 *  @param lives its lives
 */
case class Enemy(id: Int, position: Position, lives: Int) extends LivingEntity with EnemyCharacter {
  val pointsKilling: Int = 20
  private var disableBehavior: Boolean = false
  private val movementRate: Int = 20

  import Scala2P._
  val engine: Term => Seq[Option[Point]] = prolog("""
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1+1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1-1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1+1, X2 is X1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1-1, X2 is X1.
  """)

  override def movementBehaviour: Option[EnemyCharacter] =
    nextInt(movementRate) match {
      case 0 => Some(Enemy(id, Position(engine("moveAlt(" + position.point.x + "," + position.point.y + ",X,Y)").head.get, position.dir), lives))
      case _ => None
    }

  /*
  override def movementBehaviour: Option[EnemyCharacter] = {
    if (!disableBehavior) {
      nextInt(cases) match {
        case 0 => Some(Enemy(id, changePosition(position, Up), lives))
        case 1 => Some(Enemy(id, changePosition(position, Down), lives))
        case 2 => Some(Enemy(id, changePosition(position, Left), lives))
        case 3 => Some(Enemy(id, changePosition(position, Right), lives))
        case _ => None
      }
    } else {
      None
    }
  }*/

  def onTestingMode(): Unit = {
    disableBehavior = true
  }

}

object Enemy {
  val maxLife: Int = 5

  def apply(p: Position): Enemy = Enemy(nextId, p, maxLife)
}