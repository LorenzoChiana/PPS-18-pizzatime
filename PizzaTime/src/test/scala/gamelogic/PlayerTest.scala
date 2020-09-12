package gamelogic

import GameState.nextStep
import Entity.nearPoint
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.{Down, Left, Position, Right, Up}

class PlayerTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    playerName = "Player1",
    initialPlayerPosition = Position(Arena.center, Some(Right)),
    initialPlayerLife = 3,
    initialPlayerScore = 0,
    scoreToIncrease = 5,
    bonusLifePoint = nearPoint(Arena.center, Right),
    bonusScorePoint = nearPoint(nearPoint(Arena.center, Right), Right),
  )
  import staticArena.arena._
  import staticArena._

  "The player" should "have 'Player1' as name" in {
    player.playerName shouldBe "Player1"
  }

  it should "walk over bonuses" in {
    staticArena.createScenario1()

    player move Right
    player.position.point shouldEqual staticArena.bonusLifePoint

    player move Right
    player.position.point shouldEqual staticArena.bonusScorePoint
  }

  it should "increase his life if he steps on BonusLife" in {
    staticArena.createScenario2()

    player.lives shouldEqual initialPlayerLife
    nextStep(Some(Right), None)
    player.lives should be > initialPlayerLife
  }

  it should "increase his score of he steps on BonusPoint" in {
    staticArena.createScenario3()

    player.score shouldBe initialPlayerScore
    nextStep(Some(Right), None)
    player.score shouldBe initialPlayerScore + scoreToIncrease
  }

  it should "decrease his life if he collides with an enemy" in {
    staticArena.createScenario4()

    player.lives shouldBe initialPlayerLife
    nextStep(Some(Right), None)
    player.lives should be < initialPlayerLife
  }

  it should "shoot in all directions" in {
    staticArena.createEmptyScenario()

    List(Right, Left, Up, Down).foreach(direction => {
      player changeDirection direction
      nextStep(None, Some(direction))
      val bulletNearPlayerAndSameDirection = Bullet(Position(nearPoint(player.position.point, direction), Some(direction)))
      bullets should contain (bulletNearPlayerAndSameDirection)
    })
  }
}
