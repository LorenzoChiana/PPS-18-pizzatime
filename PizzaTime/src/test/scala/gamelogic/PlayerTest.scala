package gamelogic

import gamelogic.GameState.startGame
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
  import arena._

  "The player" should "have 'Player1' as name" in {
    assert(player.playerName == "Player1")
  }

  it should "be in the center of the map" in {
    assert(player.position.point.equals(Point(centerWidth, centerHeight)))
  }

  it should "move around itself" in {
    arena.obstacles = Set()
    arena.enemies = Set()

    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Up)
    assert(player.position.point.equals(Point(centerWidth, centerHeight -1)))

    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Down)
    assert(player.position.point.equals(Point(centerWidth, centerHeight +1)))

    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Left)
    assert(player.position.point.equals(Point(centerWidth -1, centerHeight)))

    player.moveTo(Position(Point(centerWidth, centerHeight), Some(Down)))
    player.move(Right)
    assert(player.position.point.equals(Point(centerWidth +1, centerHeight)))
  }

}
