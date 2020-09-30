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
  val engine: Term => Seq[Option[Position]] = prologGetPosition("""
      move(X1, Y1, right, XY) :- X2 is X1+1, XY = (X2,Y1).
      move(X1, Y1, left, XY) :- X2 is X1-1, XY = (X2,Y1).

      calc_point(X, Y, Non_walkable_tiles, right, Point, Dir) :- move(X,Y, right, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles,left, Point, Dir); (Point = XY, Dir = right)).
			calc_point(X, Y, Non_walkable_tiles, left, Point, Dir) :- move(X,Y, left, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles,right, Point, Dir); (Point = XY, Dir = left)).
			""")

  override def movementBehaviour: Option[EnemyCharacter] = {
    var result: Seq[Option[Position]] = Seq()

    position.dir match {
      case Some(Right) | None => result = engine("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", right, Point, Dir) ")
      case Some(Left) => result = engine("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", left, Point, Dir) ")
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