package gamelogic

import gamelogic.GameState.startGame
import gamelogic.MapGenerator.gameType
import gamelogic.MovableEntity.stepPoint
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Direction, Down, Left, Point, Position, Right, Up}

class MovableEntityTest extends AnyFlatSpec with Matchers {
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

  "The player" should "move up" in moveDirectionTest(player, Up)
  it should "move down" in moveDirectionTest(player, Down)
  it should "move left" in moveDirectionTest(player, Left)
  it should "move right" in moveDirectionTest(player, Right)
  it should "move around the map" in walkAroundMapTest(player)
  it should "collide with walls" in wallsCollisionTest(player)
  it should "collide with obstacles" in obstaclesCollisionsTest(player)

  player.moveTo(Position(Point(0,0), Some(Down)))
  val enemy: Enemy = Enemy(Position(centerPoint, Some(Down)))

  "An enemy" should "move up" in moveDirectionTest(enemy, Up)
  it should "move down" in moveDirectionTest(enemy, Down)
  it should "move left" in moveDirectionTest(enemy, Left)
  it should "move right" in moveDirectionTest(enemy, Right)
  it should "move around the map" in walkAroundMapTest(enemy)
  it should "collide with walls" in wallsCollisionTest(enemy)
  it should "collide with obstacles" in obstaclesCollisionsTest(enemy)

  private def moveDirectionTest(entity: MovableEntity, direction: Direction): Unit = {
    entity moveTo Position(centerPoint, Some(direction))
    entity move direction
    entity.position.point shouldEqual stepPoint(centerPoint, direction)
  }

  private def changeDirectionTest(entity: MovableEntity): Unit = {
    entity moveTo Position(centerPoint, Some(Down))
    entity move Up
    entity.position.point shouldEqual centerPoint
    entity move Left
    entity.position.point shouldEqual centerPoint
    entity move Down
    entity.position.point shouldEqual centerPoint
    entity move Right
    entity.position.point shouldEqual centerPoint
  }

  private def walkAroundMapTest(entity: MovableEntity): Unit = {
    entity moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2) y match {
      case Odd(y) =>
        for(x <- walkableWidth._1 +1 to walkableWidth._2 ) {
          entity changeDirection Right
          entity move Right
          entity.position.point shouldEqual Point(x,y)
        }
        entity changeDirection Down
        entity move Down
      case Even(y) =>
        for (x <- walkableWidth._2 -1 to walkableWidth._1 by -1) {
          entity changeDirection Left
          entity move Left
          entity.position.point shouldEqual Point(x,y)
        }
        entity.changeDirection(Down)
        entity.move(Down)
    }
  }

  private def wallsCollisionTest(entity: MovableEntity): Unit = {
    entity moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2;
         x <- walkableWidth._1 to walkableWidth._2)
      (x, y) match {
        case (walkableWidth._1, y) =>
          //Muri più a sinistra
          entity changeDirection Left
          entity move Left
          entity.position.point shouldEqual Point(x,y)
          entity changeDirection Right
          entity move Right
        case (walkableWidth._2, y) =>
          //Muri più a destra
          entity changeDirection Right
          entity move Right
          entity.position.point shouldEqual Point(x,y)
          entity moveTo Position(Point(walkableWidth._1,y+1), Some(Down))
        case (x, walkableHeight._1) =>
          //Muri in alto
          entity changeDirection Up
          entity move Up
          entity.position.point shouldEqual Point(x,y)
          entity changeDirection Right
          entity move Right
        case (x, walkableHeight._2) =>
          //Muri in basso
          entity changeDirection Down
          entity move Down
          entity.position.point shouldEqual Point(x,y)
          entity changeDirection Right
          entity move Right
        case _ =>
          entity changeDirection Right
          entity move Right
      }
  }

  private def obstaclesCollisionsTest(entity: MovableEntity): Unit = {
    val obstaclePoint = stepPoint(centerPoint, Down)
    obstacles = obstacles + Obstacle(Position(obstaclePoint, None))

    player moveTo Position(centerPoint, Some(Down))
    player move Down
    player.position.point shouldEqual centerPoint

    obstacles = Set()
  }

  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }
}
