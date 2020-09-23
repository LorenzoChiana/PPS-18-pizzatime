package gamelogic

import gamelogic.GameState.nextStep
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Position.changePosition
import utilities.{Left, Position, Right, StaticArena}

/** Test class for [[Player]]. */
class PlayerTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    playerName = "Player1",
    initialHeroPosition = Position(Arena.center, Some(Right)),
    bonusScorePosition = changePosition(Position(Arena.center, Some(Right)), Left)
  )
  import staticArena.arena._
  import staticArena._

  "The player" should "have 'Player1' as name" in {
    player.name shouldBe "Player1"
  }

  it should "have an initial score of 0" in {
    player.score shouldBe 0
  }

  it should "increase his score of he steps on BonusPoint" in {
    nextStep(Some(Left), None)
    player.score shouldBe scoreToIncrease
  }
}
