package gamelogic

import gamelogic.GameState.startGame
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


}
