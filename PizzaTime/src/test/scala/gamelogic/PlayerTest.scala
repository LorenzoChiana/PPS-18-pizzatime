package gamelogic

import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, ToDown, ToLeft, ToRight, ToUp, Up}

class PlayerTest extends AnyFlatSpec with Matchers {
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


  "The player" should "have 'Player1' as name" in {
    println("Diff: " + difficulty)
    assert(player.playerName == "Player1")
  }

  it should "be in the center of the map" in {
    assert(player.position.point.equals(centerPoint))
  }

  it should "move up" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Up)
    assert(player.position.point.equals(ToUp(centerPoint)))
  }

  it should "move down" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Down)
    assert(player.position.point.equals(ToDown(centerPoint)))
  }

  it should "move left" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Left)
    assert(player.position.point.equals(ToLeft(centerPoint)))
  }

  it should "move right" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Right)
    assert(player.position.point.equals(ToRight(centerPoint)))
  }

  it should "move around the map" in {
    player.moveTo(Position(Point(1, 1), Some(Down)))
    for (y <- walkableHeight._1 to walkableHeight._2) y match {
      case Odd(y) =>
        for(x <- walkableWidth._1 +1 to walkableWidth._2 ) {
          player.move(Right)
          assert(player.position.point.equals(Point(x,y)))
        }
        player.move(Down)
      case Even(y) =>
        for (x <- walkableWidth._2 -1 to walkableWidth._1 by -1) {
          player.move(Left)
          assert(player.position.point.equals(Point(x,y)))
        }
        player.move(Down)
    }
  }

  it should "collide with the walls" in {
    player.moveTo(Position(Point(1, 1), Some(Down)))
    for (y <- walkableHeight._1 to walkableHeight._2;
         x <- walkableWidth._1 to walkableWidth._2)
      (x, y) match {
        case (walkableWidth._1, y) =>
          //Muri più a sinistra
          player.move(Left)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case (walkableWidth._2, y) =>
          //Muri più a destra
          player.move(Right)
          assert(player.position.point.equals(Point(x,y)))
          player.moveTo(Position(Point(walkableWidth._1,y+1), Some(Down)))
        case (x, walkableHeight._1) =>
          //Muri in alto
          player.move(Up)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case (x, walkableHeight._2) =>
          //Muri in basso
          player.move(Down)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case _ => player.move(Right)
      }
  }

  it should "walk over bonuses and not obstacles" in {

    val bonusLifePoint = ToRight(centerPoint)
    val bonusScorePoint = ToLeft(centerPoint)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Right)
    assert(player.position.point.equals(bonusLifePoint))

    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Left)
    assert(player.position.point.equals(bonusScorePoint))

    val obstaclePoint = ToDown(centerPoint)
    obstacles = obstacles + Obstacle(Position(obstaclePoint, None))

    player.moveTo(Position(centerPoint, Some(Down)))
    player.move(Down)
    assert(player.position.point.equals(centerPoint))

    collectibles = Set()
    obstacles = Set()
  }

  it should "increase his life if he steps on BonusLife" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.lives = 1
    collectibles = collectibles + BonusLife(Position(ToRight(centerPoint), None))
    assert(player.lives == 1)
    nextStep(Some(Right), None)
    assert(player.lives > 1)

    collectibles = Set()
  }

  it should "increase his score of he steps on BonusPoint" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.score = 0
    val scoreToIncrease = 5
    collectibles = collectibles + BonusScore(Position(ToRight(centerPoint), None), scoreToIncrease)
    assert(player.score == 0)
    nextStep(Some(Right), None)
    assert(player.score == scoreToIncrease)

    collectibles = Set()
  }

  it should "decrease his life is he collides with an enemy" in {
    player.moveTo(Position(centerPoint, Some(Down)))
    player.lives = 5
    enemies = enemies + Enemy(Position(ToRight(centerPoint), Some(Left)))
    assert(player.lives == 5)
    nextStep(Some(Right), None)
    assert(player.lives < 5)

    enemies = Set()
  }

  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }

  object toUp {
    def unapply(point: Point): Option[Point] = Some(Point(point.x, point.y -1))
  }
}
