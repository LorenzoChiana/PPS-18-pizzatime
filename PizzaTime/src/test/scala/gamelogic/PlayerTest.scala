package gamelogic

import gamelogic.GameState.{arenaHeight, arenaWidth, startGame}
import gamelogic.MapGenerator.gameType
import gamemanager.handlers.PreferencesHandler.difficulty
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utilities.Difficulty.Medium
import utilities.Point
import utilities.{Direction, Down, Left, Position, Right, Up}

class PlayerTest extends AnyFlatSpec with Matchers {
  startGame("Player1", gameType(Medium))
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
    println("Player: "+ player.position.point)
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



  object Even {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }

  object Odd {
    def unapply(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
  }
}
