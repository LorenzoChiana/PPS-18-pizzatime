package gamelogic

import alice.tuprolog.Term
import utilities.IdGenerator.nextId
import utilities.{Point, Position, Scala2P}

/** An enemy character.
 *
 *  @param position its position
 *  @param lives its lives
 */
case class EnemyWithRandomMove(id: Int, position: Position, lives: Int) extends LivingEntity with EnemyCharacter {
  val pointsKilling: Int = 20
  private var disableBehavior: Boolean = false

  import Scala2P._
  val engine: Term => Seq[Option[Point]] = prologGetPoint("""
	    move(X1, Y1, 0, XY) :- X2 is X1+1, XY = (X2,Y1).
      move(X1, Y1, 1, XY) :- X2 is X1-1, XY = (X2,Y1).
      move(X1, Y1, 2, XY) :- Y2 is Y1+1, XY = (X1,Y2).
      move(X1, Y1, 3, XY) :- Y2 is Y1-1, XY = (X1,Y2).

      calc_point(X, Y, Non_walkable_tiles, Point) :- rand_int(4, Rnd), move(X,Y, Rnd, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles, XY); Point = XY).
""")

  override def movementBehaviour: Option[EnemyCharacter] = {
    val result = engine("calc_point(" + position.point.x + ", " + position.point.y + "," + nonWalkableTiles + ", Point) " )
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