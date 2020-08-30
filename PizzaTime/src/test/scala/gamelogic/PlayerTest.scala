package gamelogic

import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamelogic.MovableEntity.stepPoint
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, Up}

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

  it should "walk over bonuses" in {
    val bonusLifePoint = stepPoint(centerPoint, Right)
    val bonusScorePoint = stepPoint(centerPoint, Left)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    player moveTo Position(centerPoint, Some(Right))
    player move Right
    player.position.point shouldEqual bonusLifePoint

    player moveTo Position(centerPoint, Some(Left))
    player move Left
    player.position.point shouldEqual bonusScorePoint

    collectibles = Set()
  }

  it should "increase his life if he steps on BonusLife" in {
    player moveTo Position(centerPoint, Some(Right))
    player.lives = 1
    collectibles = collectibles + BonusLife(Position(stepPoint(centerPoint, Right), None))
    player.lives shouldEqual 1
    nextStep(Some(Right), None)
    player.lives should be > 1

    collectibles = Set()
  }

  it should "increase his score of he steps on BonusPoint" in {
    player moveTo Position(centerPoint, Some(Right))
    player.score = 0
    val scoreToIncrease = 5
    collectibles = collectibles + BonusScore(Position(stepPoint(centerPoint, Right), None), scoreToIncrease)
    player.score shouldBe 0
    nextStep(Some(Right), None)
    player.score shouldBe scoreToIncrease

    collectibles = Set()
  }

  it should "decrease his life is he collides with an enemy" in {
    player moveTo Position(centerPoint, Some(Right))
    player.lives = 5
    enemies = enemies + Enemy(Position(stepPoint(centerPoint, Right), Some(Left)))
    player.lives shouldBe 5
    nextStep(Some(Right), None)
    player.lives should be < 5

    enemies = Set()
  }
}
