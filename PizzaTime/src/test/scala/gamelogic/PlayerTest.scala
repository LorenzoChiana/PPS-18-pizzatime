package gamelogic

import GameState.nextStep
import Entity.nearPoint
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.{Direction, Down, Left, Position, Right, Up}

class PlayerTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    playerName = "Player1",
    initialPlayerPosition = Position(Arena.center, Some(Right)),
    initialPlayerLife = 3,
    initialEnemyPosition = Position(nearPoint(Arena.center, Up), Some(Down)),
    bonusLifePoint = nearPoint(Arena.center, Right),
    bonusScorePoint = nearPoint(Arena.center, Left)
  )
  import staticArena.arena._
  import staticArena._

  "The player" should "have 'Player1' as name" in {
    player.playerName shouldBe "Player1"
  }

  it should "walk over bonuses" in {
    player move Right
    player.position.point shouldEqual staticArena.bonusLifePoint

    player moveTo initialPlayerPosition
    player move Left
    player.position.point shouldEqual staticArena.bonusScorePoint
  }

  it should "increase his life if he steps on BonusLife" in {
    player moveTo initialPlayerPosition
    player.lives shouldEqual initialPlayerLife
    nextStep(Some(Right), None)
    player.lives should be (initialPlayerLife + 1)
  }

  it should "increase his score of he steps on BonusPoint" in {
    player moveTo initialPlayerPosition
    player.score shouldBe initialPlayerScore
    nextStep(Some(Left), None)
    player.score shouldBe initialPlayerScore + scoreToIncrease
  }

  it should "decrease his life if he collides with an enemy" in {
    player moveTo initialPlayerPosition
    player.lives = initialPlayerLife

    player.lives shouldBe initialPlayerLife
    nextStep(Some(Up), None)
    player.lives should be < initialPlayerLife
  }

  it should "shoot in all directions" in {
    staticArena.createEmptyScenario()
    List(Right, Left, Up, Down).foreach(direction => {
      shootOn(direction)
      val bulletNearPlayerAndSameDirection = Bullet(Position(nearPoint(player.position.point, direction), Some(direction)))
      bullets should contain (bulletNearPlayerAndSameDirection)
    })
  }

  private def shootOn(direction: Direction): Unit = {
    player changeDirection direction
    nextStep(None, Some(direction))
  }
}
