package gamelogic

import GameState.nextStep
import Entity._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.{Down, Left, Point, Position, Right}

class EnemyTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialPlayerPosition = Position(Point(0, 0), Some(Down)),
    initialEnemyPosition = Position(Arena.center, Some(Down)),
    otherEnemyPoint = nearPoint(Arena.center, Right),
    bonusLifePoint = nearPoint(Arena.center, Right),
    bonusScorePoint = nearPoint(Arena.center, Left)
  )
  import staticArena.arena._
  import staticArena._

  "An enemy" should "have initialized life and points killing" in {
    enemy.lives should be > 0
    enemy.pointsKilling should be > 0
  }

  it should "collide with bonuses" in {
    staticArena.createScenario1()

    enemy move Right
    enemy.position.point should not equal bonusLifePoint
    enemy.position.point shouldBe initialEnemyPosition.point

    enemy move Left
    enemy.position.point should not equal bonusScorePoint
    enemy.position.point shouldBe initialEnemyPosition.point
  }

  it should "collide with other enemies" in {
    staticArena.createScenario5()

    enemy move Right
    enemy.position.point should not equal otherEnemyPoint
    enemy.position.point shouldBe initialEnemyPosition.point
  }

  it should "lose his life when he collides with a bullet" in {
    staticArena.createScenario6()

    enemy.lives shouldBe initialEnemyLife
    nextStep(None, Some(Right))
    enemy.lives should be < initialEnemyLife
  }

  it should "take the player's life away when he collides with him" in {
    staticArena.createScenario6()

    player.lives shouldBe initialPlayerLife
    enemy move Left
    nextStep(None, None)
    player.lives should be < initialPlayerLife
  }
}
