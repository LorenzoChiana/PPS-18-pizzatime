package gamelogic

import gamelogic.GameState.startGame
import gamelogic.MapGenerator.gameType
import gamelogic.MovableEntity.stepPoint
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, Up}

class EnemyTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", gameType(Easy))
  val arena: GameMap = GameState.arena.get
  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth-2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight-2)
  val centerPoint: Point = Point(difficulty.arenaWidth/2, difficulty.arenaHeight/2)

  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()
  player.moveTo(Position(Point(0,0), Some(Down)))

  val enemy: Enemy = Enemy(Position(centerPoint, Some(Down)))

  "An enemy" should "move up" in {
    enemy moveTo Position(centerPoint, Some(Up))
    enemy move Up
    enemy.position.point shouldEqual stepPoint(centerPoint, Up)
  }

  it should "move down" in {
    enemy moveTo Position(centerPoint, Some(Down))
    enemy move Down
    enemy.position.point shouldEqual stepPoint(centerPoint, Down)
  }

  it should "move left" in {
    enemy moveTo Position(centerPoint, Some(Left))
    enemy move Left
    enemy.position.point shouldEqual stepPoint(centerPoint, Left)
  }

  it should "move right" in {
    enemy moveTo Position(centerPoint, Some(Right))
    enemy move Right
    enemy .position.point shouldEqual stepPoint(centerPoint, Right)
  }

  it should "move around the map" in {
    enemy moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2) y match {
      case Odd(y) =>
        for(x <- walkableWidth._1 +1 to walkableWidth._2 ) {
          enemy changeDirection Right
          enemy move Right
          enemy.position.point shouldEqual Point(x,y)
        }
        enemy changeDirection Down
        enemy move Down
      case Even(y) =>
        for (x <- walkableWidth._2 -1 to walkableWidth._1 by -1) {
          enemy changeDirection Left
          enemy move Left
          enemy.position.point shouldEqual Point(x,y)
        }
        enemy.changeDirection(Down)
        enemy.move(Down)
    }
  }

  it should "collide with the walls" in {
    enemy moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2;
         x <- walkableWidth._1 to walkableWidth._2)
      (x, y) match {
        case (walkableWidth._1, y) =>
          //Muri più a sinistra
          enemy changeDirection Left
          enemy move Left
          enemy.position.point shouldEqual Point(x,y)
          enemy changeDirection Right
          enemy move Right
        case (walkableWidth._2, y) =>
          //Muri più a destra
          enemy changeDirection Right
          enemy move Right
          enemy.position.point shouldEqual Point(x,y)
          enemy moveTo Position(Point(walkableWidth._1,y+1), Some(Down))
        case (x, walkableHeight._1) =>
          //Muri in alto
          enemy changeDirection Up
          enemy move Up
          enemy.position.point shouldEqual Point(x,y)
          enemy changeDirection Right
          enemy move Right
        case (x, walkableHeight._2) =>
          //Muri in basso
          enemy changeDirection Down
          enemy move Down
          enemy.position.point shouldEqual Point(x,y)
          enemy changeDirection Right
          enemy move Right
        case _ =>
          enemy changeDirection Right
          enemy move Right
      }
  }

  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }
}
