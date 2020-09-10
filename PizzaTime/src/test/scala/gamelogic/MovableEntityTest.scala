package gamelogic

import GameState.startGame
import Entity._
import Arena.containsWall
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import utilities.Difficulty._
import utilities.{Direction, Down, Left, Point, Position, Right, Up}

class MovableEntityTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", MapGenerator(Easy))
  val arena: GameMap = GameState.arena.get
  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth-2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight-2)

  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()

  val enemy: Enemy = Enemy(Position(Point(0,0), Some(Down)))
  val bullet: Bullet = Bullet(Position(Point(0,0), Some(Down)))

  List(player, enemy, bullet).foreach(entity => {
    entity moveTo Position(Arena.center, Some(Down))
    movableEntityTests(entity)
    entity moveTo Position(Point(0,0), Some(Down))
  })

  private def movableEntityTests(entity: MovableEntity): Unit = entity match {
    case _: Bullet => basicMovableTests("A Bullet", entity)
    case _: Player => advancedMovableTests("The player", entity)
    case _: Enemy => advancedMovableTests("An enemy", entity)
  }

  private def basicMovableTests(entityName: String, entity: MovableEntity): Unit = {
    entityName should "move up" in moveDirectionTest(entity, Up)
    it should "move down" in moveDirectionTest(entity, Down)
    it should "move left" in moveDirectionTest(entity, Left)
    it should "move right" in moveDirectionTest(entity, Right)
    it should "collide with obstacles" in obstaclesCollisionsTest(entity)
    it should "collide with walls" in wallsCollisionTest(entity)
  }

  private def advancedMovableTests(entityName: String, entity: MovableEntity): Unit = {
    basicMovableTests(entityName, entity)
    entityName should "move around the map" in walkAroundMapTest(entity)
  }

  private def moveDirectionTest(entity: MovableEntity, direction: Direction): Unit = {
    entity moveTo Position(Arena.center, Some(direction))
    entity move direction
    entity.position.point shouldEqual nearPoint(Arena.center, direction)
  }

  private def walkAroundMapTest(entity: MovableEntity): Unit = {
    val left = walkableWidth._1
    val right = walkableWidth._2
    val up = walkableHeight._1
    val down = walkableHeight._2
    entity moveTo Position(Point(1, 1), Some(Down))

    for (y <- up to down) {
      y match {
        case Odd(y) =>
          for(x <- left +1 to right) {
            entity changeDirectionAndMove Right
            entity.position.point shouldEqual Point(x,y)
          }
        case Even(y) =>
          for(x <- right -1 to left by -1) {
            entity changeDirectionAndMove Left
            entity.position.point shouldEqual Point(x,y)
          }
      }
      entity changeDirectionAndMove Down
    }
  }

  private def wallsCollisionTest(entity: MovableEntity): Unit = {
    List(Up, Down, Left, Right).foreach(direction => {
      entity moveTo Position(Arena.center, Some(direction))
      while(entity canMoveIn nearPoint(entity.position.point, direction))
        entity move direction

      containsWall(nearPoint(entity.position.point, direction)) shouldBe true
    })

  }

  private def obstaclesCollisionsTest(entity: MovableEntity): Unit = {
    val obstaclePoint = nearPoint(Arena.center, Down)
    obstacles = obstacles + Obstacle(Position(obstaclePoint, None))

    entity moveTo Position(Arena.center, Some(Down))
    entity move Down
    entity.position.point shouldEqual Arena.center

    obstacles = Set()
  }

  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }
}
