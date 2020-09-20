package gamelogic

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Position.changePosition
import utilities.{Down, Left, Position, Right, StaticArena, Up}

/** Test class for [[MovableEntity]] */
class MovableEntityTest extends AnyFlatSpec with Matchers {
  val staticArena: StaticArena = StaticArena(
    initialHeroPosition = Position(Arena.center, Some(Down)),
    initialEnemyPosition = Position(Arena.center, Some(Down))
  )
  import staticArena.arena._
  import staticArena._

  "A movable entity" should "move up" in {
    (hero move Up).position shouldBe changePosition(hero.position, Up)
    (enemy move Up).position shouldBe changePosition(enemy.position, Up)
  }

  it should "move down" in {
    (hero move Down).position shouldBe changePosition(hero.position, Down)
    (enemy move Down).position shouldBe changePosition(enemy.position, Down)
  }

  it should "move right" in {
    (hero move Right).position shouldBe changePosition(hero.position, Right)
    (enemy move Right).position shouldBe changePosition(enemy.position, Right)
  }

  it should "move left" in {
    (hero move Left).position shouldBe changePosition(hero.position, Left)
    (enemy move Left).position shouldBe changePosition(enemy.position, Left)
  }

  it should "change direction" in {
    List(Up, Down, Right, Left).foreach(direction => {
      (hero changeDirection direction).position.dir shouldBe Some(direction)
      (enemy changeDirection direction).position.dir shouldBe Some(direction)
    })
  }
}
