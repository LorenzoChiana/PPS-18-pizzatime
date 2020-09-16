package gamelogic

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

/** Test class for [[Hero]] */
class PlayerTest extends AnyFlatSpec with Matchers {
  /* val staticArena: StaticArena = StaticArena(
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
    hero.playerName shouldBe "Player1"
  }

  it should "walk over bonuses" in {
    hero move Right
    hero.position.point shouldEqual staticArena.bonusLifePoint

    hero moveTo initialPlayerPosition
    hero move Left
    hero.position.point shouldEqual staticArena.bonusScorePoint
  }

  it should "increase his life if he steps on BonusLife" in {
    hero moveTo initialPlayerPosition
    hero.lives shouldEqual initialPlayerLife
    nextStep(Some(Right), None)
    hero.lives should be (initialPlayerLife + 1)
  }

  it should "increase his score of he steps on BonusPoint" in {
    hero moveTo initialPlayerPosition
    hero.score shouldBe initialPlayerScore
    nextStep(Some(Left), None)
    hero.score shouldBe initialPlayerScore + scoreToIncrease
  }

  it should "decrease his life if he collides with an enemy" in {
    hero moveTo initialPlayerPosition
    hero.lives = initialPlayerLife

    hero.lives shouldBe initialPlayerLife
    nextStep(Some(Up), None)
    hero.lives should be < initialPlayerLife
  }

  it should "shoot in all directions" in {
    staticArena.createEmptyScenario()
    List(Right, Left, Up, Down).foreach(direction => {
      shootOn(direction)
      val bulletNearPlayerAndSameDirection = Bullet(Position(nearPoint(hero.position.point, direction), Some(direction)))
      bullets should contain (bulletNearPlayerAndSameDirection)
    })
  }

  private def shootOn(direction: Direction): Unit = {
    hero changeDirection direction
    nextStep(None, Some(direction))
  }

   */
}
