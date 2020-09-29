package gamelogic

import alice.tuprolog.Term
import utilities.IdGenerator.nextId
import utilities.{Point, Position, Scala2P}

import scala.util.Random.nextInt

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
      move(X1,Y1,X2,Y2) :- X2 is X1+1, Y2 is Y1.
      move(X1,Y1,X2,Y2) :- X2 is X1-1, Y2 is Y1.
      move(X1,Y1,X2,Y2) :- Y2 is Y1+1, X2 is X1.
      move(X1,Y1,X2,Y2) :- Y2 is Y1-1, X2 is X1.

      %search_point(+List, +H, -B) B true if H is present
      search([H|T], Non_walkable_tiles, XY) :- findall(H, member(H, Non_walkable_tiles), R), member(_, R) -> search(T, Non_walkable_tiles, XY) ; XY = H.

      calc_point(X, Y, Non_walkable_tiles, XY) :- findall((X1,Y1), move(X,Y,X1,Y1), R), search(R, Non_walkable_tiles, XY).
  """)

  override def movementBehaviour: Option[EnemyCharacter] = {
    println(engine("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", Point) " ).head.get)
    nextInt(movementRate) match {
      case 0 => { val en = Enemy(id, Position(engine("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", Point) " ).head.get, position.dir), lives)
        println(en)
        Some(en)
      }
      case _ => None
    }
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