package gamelogic

import gamelogic.GameState.{nextStep, startGame}
import gamelogic.MapGenerator.gameType
import gamelogic.MovableEntity.stepPoint
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty.Easy
import utilities.{Down, Left, Point, Position, Right, Up}

class EnemyTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", gameType(Easy))
  val arena: GameMap = GameState.arena.get
  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth-2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight-2)
  val centerPoint: Point = Point(difficulty.arenaWidth/2, difficulty.arenaHeight/2)

  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()
  player.moveTo(Position(Point(0,0), Some(Down)))

  val enemy: Enemy = Enemy(Position(centerPoint, Some(Down)))

  "An enemy" should "have initialized life and points killing" in {
    enemy.lives should be > 0
    enemy.pointsKilling should be > 0
  }

  it should "collide with bonuses" in {
    val bonusLifePoint = stepPoint(centerPoint, Right)
    val bonusScorePoint = stepPoint(centerPoint, Left)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    enemy moveTo Position(centerPoint, Some(Right))
    enemy move Right
    enemy.position.point should not equal bonusLifePoint
    enemy.position.point shouldBe centerPoint

    enemy moveTo Position(centerPoint, Some(Left))
    enemy move Left
    enemy.position.point should not equal bonusScorePoint
    enemy.position.point shouldBe centerPoint

    collectibles = Set()
  }

  it should "collide with other enemies" in {
    enemy moveTo Position(centerPoint, Some(Right))
    val otherEnemyPoint: Point = stepPoint(centerPoint, Right)
    val enemy2 = Enemy(Position(otherEnemyPoint, Some(Right)))
    enemies = enemies + enemy + enemy2

    enemy move Right
    enemy.position.point should not equal otherEnemyPoint
    enemy.position.point shouldBe centerPoint

    enemies = Set()
  }

  it should "lose his life when he collides with a bullet" in {
    player moveTo Position(centerPoint, Some(Right))
    enemy moveTo Position(stepPoint(centerPoint, Right), Some(Right))
    enemies = enemies + enemy

    enemy.lives = 5
    enemy.lives shouldBe 5
    nextStep(None, Some(Right))
    enemy.lives should be < 5

    enemies = Set()
  }

  it should "take the player's life away when he collides with him" in {
    player moveTo Position(centerPoint, Some(Right))
    enemy moveTo Position(stepPoint(centerPoint, Right), Some(Left))
    enemies = enemies + enemy

    player.lives = 5
    player.lives shouldBe 5
    enemy move Left
    nextStep(None, None)
    player.lives should be < 5

    enemies = Set()
  }
}
