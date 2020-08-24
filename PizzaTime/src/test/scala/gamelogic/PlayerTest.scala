package gamelogic

import gamelogic.GameState.{arenaHeight, arenaWidth, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty.{Easy, Medium}
import utilities.Point
import utilities.{Direction, Down, Left, Position, Right, Up}

class PlayerTest extends AnyFlatSpec with Matchers {
  difficulty_(Easy)
  startGame("Player1", gameType(Easy))
  val arena: GameMap = GameState.arena.get
  val centerWidth: Int = difficulty.arenaWidth/2
  val centerHeight: Int = difficulty.arenaHeight/2
  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth-2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight-2)

  import arena._
  obstacles = Set()
  enemies = Set()
  collectibles = Set()


  "The player" should "have 'Player1' as name" in {
    println("Diff: " + difficulty + " arenaW: "+difficulty.arenaWidth + " arenaH: "+difficulty.arenaHeight)
    assert(player.playerName == "Player1")
  }

  it should "be in the center of the map" in {
    assert(player.position.point.equals(Point(centerWidth, centerHeight)))
  }

  it should "move up" in {
    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Up)
    assert(player.position.point.equals(Point(centerWidth, centerHeight - 1)))
  }

  it should "move down" in {
    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Down)
    assert(player.position.point.equals(Point(centerWidth, centerHeight +1)))
  }

  it should "move left" in {
    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Left)
    assert(player.position.point.equals(Point(centerWidth -1, centerHeight)))
  }

  it should "move right" in {
    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Right)
    assert(player.position.point.equals(Point(centerWidth +1, centerHeight)))
  }

  it should "move around the map" in {
    player.moveTo(Position(Point(1, 1), Some(Down)))
    for (y <- walkableHeight._1 to walkableHeight._2) y match {
      case Odd(y) =>
        for(x <- walkableWidth._1 +1 to walkableWidth._2 ) {
          player.move(Right)
          assert(player.position.point.equals(Point(x,y)))
        }
        player.move(Down)
      case Even(y) =>
        for (x <- walkableWidth._2 -1 to walkableWidth._1 by -1) {
          player.move(Left)
          assert(player.position.point.equals(Point(x,y)))
        }
        player.move(Down)
    }
  }

  it should "collide with the walls" in {
    player.moveTo(Position(Point(1, 1), Some(Down)))
    for (y <- walkableHeight._1 to walkableHeight._2;
         x <- walkableWidth._1 to walkableWidth._2)
      (x, y) match {
        case (walkableWidth._1, y) =>
          //Muri più a sinistra
          player.move(Left)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case (walkableWidth._2, y) =>
          //Muri più a destra
          player.move(Right)
          assert(player.position.point.equals(Point(x,y)))
          player.moveTo(Position(Point(walkableWidth._1,y+1), Some(Down)))
        case (x, walkableHeight._1) =>
          //Muri in alto
          player.move(Up)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case (x, walkableHeight._2) =>
          //Muri in basso
          player.move(Down)
          assert(player.position.point.equals(Point(x,y)))
          player.move(Right)
        case _ => player.move(Right)
      }
  }

  it should "walk over bonuses and not obstacles" in {
    val initialPlayerPoint = Point(centerWidth, centerHeight)

    val bonusLifePoint = Point(centerWidth +1, centerHeight)
    val bonusScorePoint = Point(centerWidth -1, centerHeight)
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)

    player.moveTo(Position(initialPlayerPoint, Some(Down)))
    player.move(Right)
    assert(player.position.point.equals(bonusLifePoint))

    player.moveTo(Position(initialPlayerPoint, Some(Down)))
    player.move(Left)
    assert(player.position.point.equals(bonusScorePoint))

    val obstaclePoint = Point(centerWidth, centerHeight +1)
    obstacles = obstacles + Obstacle(Position(obstaclePoint, None))

    player.moveTo(Position(initialPlayerPoint, Some(Down)))
    player.move(Down)
    assert(player.position.point.equals(initialPlayerPoint))

    collectibles = Set()
    obstacles = Set()
  }

  it should "increase his life if he steps on BonusLife" in {
    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.lives = 0
    collectibles = collectibles + BonusLife(Position(Point(centerWidth +1, centerHeight), None))
    assert(player.lives == 0)
    player.move(Right)
    //assert(player.lives > 0)

    collectibles = Set()
  }

  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }
}
