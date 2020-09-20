package gamelogic

import GameState.nextStep
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Position.changePosition
import utilities.{Down, Left, Position, Right, StaticArena, Up}

/** Test class for [[Enemy]] */
class EnemyTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialEnemyPosition = Position(Arena.center, Some(Down)),
    initialHeroPosition = changePosition(Position(Arena.center, Some(Down)), Up),
    bonusLifePosition = changePosition(Position(Arena.center, Some(Down)), Right),
    bonusScorePosition = changePosition(Position(Arena.center, Some(Down)), Left)
  )
  import staticArena.arena._
  import staticArena._

  "An enemy" should "have initialized life and points killing" in {
    enemy.lives should be > 0
    enemy.pointsKilling should be > 0
  }

  it should "lose his life when he collides with a bullet" in {
    enemy.lives shouldBe Enemy.maxLife
    nextStep(None, Some(Down))
    enemies.last.lives shouldBe (Enemy.maxLife - 1)
  }
}
