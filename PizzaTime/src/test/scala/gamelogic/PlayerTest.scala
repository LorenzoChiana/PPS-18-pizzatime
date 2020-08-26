package gamelogic

import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
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
    player.playerName shouldBe "Player1"
  }

  it should "be in the center of the map" in {
    player.position.point shouldEqual centerPoint
  }

  it should "only change direction if the user expresses an intention to move in a different direction that it is" in {
    player moveTo Position(centerPoint, Some(Down))
    player move Up
    player.position.point shouldEqual centerPoint
    player move Left
    player.position.point shouldEqual centerPoint
    player move Down
    player.position.point shouldEqual centerPoint
    player move Right
    player.position.point shouldEqual centerPoint
  }

  it should "move up" in {
    player moveTo Position(centerPoint, Some(Up))
    player move Up
    player.position.point shouldEqual ToUp(centerPoint)
  }

  it should "move down" in {
    player moveTo Position(centerPoint, Some(Down))
    player move Down
    player.position.point shouldEqual ToDown(centerPoint)
  }

  it should "move left" in {
    player moveTo Position(centerPoint, Some(Left))
    player move Left
    player.position.point shouldEqual ToLeft(centerPoint)
  }

  it should "move right" in {
    player moveTo Position(centerPoint, Some(Right))
    player move Right
    player.position.point shouldEqual ToRight(centerPoint)
  }

  it should "move around the map" in {
    player moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2) y match {
      case Odd(y) =>
        for(x <- walkableWidth._1 +1 to walkableWidth._2 ) {
          player changeDirection Right
          player move Right
          player.position.point shouldEqual Point(x,y)
        }
        player changeDirection Down
        player move Down
      case Even(y) =>
        for (x <- walkableWidth._2 -1 to walkableWidth._1 by -1) {
          player changeDirection Left
          player move Left
          player.position.point shouldEqual Point(x,y)
        }
        player.changeDirection(Down)
        player.move(Down)
    }
  }

  it should "collide with the walls" in {
    player moveTo Position(Point(1, 1), Some(Down))
    for (y <- walkableHeight._1 to walkableHeight._2;
         x <- walkableWidth._1 to walkableWidth._2)
      (x, y) match {
        case (walkableWidth._1, y) =>
          //Muri più a sinistra
          player changeDirection Left
          player move Left
          player.position.point shouldEqual Point(x,y)
          player changeDirection Right
          player move Right
        case (walkableWidth._2, y) =>
          //Muri più a destra
          player changeDirection Right
          player move Right
          player.position.point shouldEqual Point(x,y)
          player moveTo Position(Point(walkableWidth._1,y+1), Some(Down))
        case (x, walkableHeight._1) =>
          //Muri in alto
          player changeDirection Up
          player move Up
          player.position.point shouldEqual Point(x,y)
          player changeDirection Right
          player move Right
        case (x, walkableHeight._2) =>
          //Muri in basso
          player changeDirection Down
          player move Down
          player.position.point shouldEqual Point(x,y)
          player changeDirection Right
          player move Right
        case _ =>
          player changeDirection Right
          player move Right
      }
  }

  it should "walk over bonuses and not obstacles" in {

    val bonusLifePoint = ToRight(centerPoint)
    val bonusScorePoint = ToLeft(centerPoint)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    player moveTo Position(centerPoint, Some(Right))
    player move Right
    player.position.point shouldEqual bonusLifePoint

    player moveTo Position(centerPoint, Some(Left))
    player move Left
    player.position.point shouldEqual bonusScorePoint

    val obstaclePoint = ToDown(centerPoint)
    obstacles = obstacles + Obstacle(Position(obstaclePoint, None))

    player moveTo Position(centerPoint, Some(Down))
    player move Down
    player.position.point shouldEqual centerPoint

    collectibles = Set()
    obstacles = Set()
  }

  it should "increase his life if he steps on BonusLife" in {
    player moveTo Position(centerPoint, Some(Right))
    player.lives = 1
    collectibles = collectibles + BonusLife(Position(ToRight(centerPoint), None))
    player.lives shouldEqual 1
    nextStep(Some(Right), None)
    player.lives should be > 1

    collectibles = Set()
  }

  it should "increase his score of he steps on BonusPoint" in {
    player moveTo Position(centerPoint, Some(Right))
    player.score = 0
    val scoreToIncrease = 5
    collectibles = collectibles + BonusScore(Position(ToRight(centerPoint), None), scoreToIncrease)
    player.score shouldBe 0
    nextStep(Some(Right), None)
    player.score shouldBe scoreToIncrease

    collectibles = Set()
  }

  it should "decrease his life is he collides with an enemy" in {
    player moveTo Position(centerPoint, Some(Right))
    player.lives = 5
    enemies = enemies + Enemy(Position(ToRight(centerPoint), Some(Left)))
    player.lives shouldBe 5
    nextStep(Some(Right), None)
    player.lives should be < 5

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
