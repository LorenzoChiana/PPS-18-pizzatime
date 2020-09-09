package gamelogic

import GameState.{nextStep, startGame}
import MapGenerator.gameType
import Entity._
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

  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()


  "The player" should "have 'Player1' as name" in {
    player.playerName shouldBe "Player1"
  }

  it should "walk over bonuses" in {
    val bonusLifePoint = nearPoint(Arena.center, Right)
    val bonusScorePoint = nearPoint(Arena.center, Left)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    player moveTo Position(Arena.center, Some(Right))
    player move Right
    player.position.point shouldEqual bonusLifePoint

    player moveTo Position(Arena.center, Some(Left))
    player move Left
    player.position.point shouldEqual bonusScorePoint

    collectibles = Set()
  }

  it should "increase his life if he steps on BonusLife" in {
    player moveTo Position(Arena.center, Some(Right))
    collectibles = collectibles + BonusLife(Position(nearPoint(Arena.center, Right), None))

    player.lives = 1
    player.lives shouldEqual 1
    nextStep(Some(Right), None)
    player.lives should be > 1

    collectibles = Set()
  }

  it should "increase his score of he steps on BonusPoint" in {
    player moveTo Position(Arena.center, Some(Right))
    val scoreToIncrease = 5
    collectibles = collectibles + BonusScore(Position(nearPoint(Arena.center, Right), None), scoreToIncrease)

    player.score = 0
    player.score shouldBe 0
    nextStep(Some(Right), None)
    player.score shouldBe scoreToIncrease

    collectibles = Set()
  }

  it should "decrease his life if he collides with an enemy" in {
    player moveTo Position(Arena.center, Some(Right))
    enemies = enemies + Enemy(Position(nearPoint(Arena.center, Right), Some(Left)))

    player.lives = 5
    player.lives shouldBe 5
    nextStep(Some(Right), None)
    player.lives should be < 5

    enemies = Set()
  }

  it should "shoot in all directions" in {
    List(Right, Left, Up, Down).foreach(direction => {
      player moveTo Position(Arena.center, Some(direction))
      bullets shouldBe Set()
      nextStep(None, Some(direction))
      bullets should not be Set()
      bullets should contain (Bullet(Position(nearPoint(Arena.center, direction), Some(direction))))
      bullets = Set()
    })
  }
}
