package gamelogic

import GameState.nextStep
import Entity._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.{Up, Down, Left, Position, Right}

/** Test class for [[Enemy]] */
class EnemyTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialEnemyPosition = Position(Arena.center, Some(Down)),
    initialPlayerPosition = Position(nearPoint(Arena.center, Up), Some(Down)),
    otherEnemyPoint = nearPoint(Arena.center, Down),
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
    enemy move Right
    enemy.position.point should not equal bonusLifePoint
    enemy.position.point shouldBe initialEnemyPosition.point

    enemy move Left
    enemy.position.point should not equal bonusScorePoint
    enemy.position.point shouldBe initialEnemyPosition.point
  }

  it should "collide with other enemies" in {
    enemy move Down
    enemy.position.point should not equal otherEnemyPoint
    enemy.position.point shouldBe initialEnemyPosition.point
  }

  it should "lose his life when he collides with a bullet" in {
    enemy.lives shouldBe initialEnemyLife
    nextStep(None, Some(Down))
    enemy.lives should be < initialEnemyLife
  }

  it should "take the player's life away when he collides with him" in {
    player.lives shouldBe initialPlayerLife
    enemy move Up
    nextStep(None, None)
    player.lives should be < initialPlayerLife
  }
}
