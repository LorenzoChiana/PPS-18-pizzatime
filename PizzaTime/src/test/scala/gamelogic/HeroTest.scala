package gamelogic

import gamelogic.GameState.nextStep
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Position.changePosition
import utilities.{Down, Left, Position, Right, StaticArena, Up}


/** Test class for [[Hero]] */
class HeroTest extends AnyFlatSpec with Matchers {
   val staticArena: StaticArena = StaticArena(
    initialPlayerPosition = Position(Arena.center, Some(Right)),
    initialEnemyPosition = changePosition(Position(Arena.center, Some(Right)), Up),
    bonusLifePosition = changePosition(Position(Arena.center, Some(Right)), Right),
    bonusScorePosition = changePosition(Position(Arena.center, Some(Right)), Left)
  )
  import staticArena.arena._

  "The hero" should "have a life of 5" in {
    hero.lives shouldEqual 5
  }

  it should "walk over score bonus" in {
    nextStep(Some(Left), None)
    hero.position.point shouldEqual staticArena.bonusScorePosition.point
    nextStep(Some(Right), None)
  }

  it should "decrease his life if he collides with an enemy" in {
    hero.lives shouldBe 5
    nextStep(Some(Up), None)
    hero.lives shouldBe 4
    nextStep(Some(Down), None)
  }

  it should "walk over life bonus and increase his life" in {
    nextStep(Some(Right), None)
    hero.position.point shouldEqual staticArena.bonusLifePosition.point
    hero.lives shouldBe 5
    nextStep(Some(Left), None)
  }

  it should "shoot in all directions" in {
    staticArena.createEmptyScenario()
    List(Right, Left, Up, Down).foreach(direction => {
      nextStep(None, Some(direction))
      bullets.last.position shouldBe changePosition(hero.position, direction)
    })
  }
}
