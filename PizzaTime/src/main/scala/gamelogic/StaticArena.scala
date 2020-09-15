package gamelogic

import gamelogic.GameState.startGame
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import utilities.Difficulty.Easy
import utilities.{Down, Point, Position, Up}

/**
 * Builder class used in tests to create a custom arena
 * @param playerName the name of player
 * @param initialPlayerPosition the position in the arena where the player starts
 * @param initialPlayerLife the player's initial life
 * @param initialPlayerScore the player's initial score
 * @param scoreToIncrease the increase of the score when collecting the score bonuses
 * @param bonusLifePoint the position in the arena of the life bonus
 * @param bonusScorePoint the position in the arena of the score bonus
 * @param initialEnemyPosition the position in the arena where the enemy starts
 * @param otherEnemyPoint the position in the arena where the second enemy starts
 * @param initialEnemyLife the enemy's initial life
 */
case class StaticArena (
  playerName: String = "PlayerName",
  initialPlayerPosition: Position = Position(Point(0, 0), Some(Down)),
  initialPlayerLife: Int = 5,
  initialPlayerScore: Int = 0,
  scoreToIncrease: Int = 5,
  bonusLifePoint: Point = Point(0, 0),
  bonusScorePoint: Point = Point(0, 0),
  initialEnemyPosition: Position = Position(Point(0, 0), Some(Down)),
  otherEnemyPoint: Point = Point(0, 0),
  initialEnemyLife: Int = 5
) {
  difficulty_(Easy)
  startGame(playerName, MapGenerator(Easy))
  val arena: GameMap = GameState.arena.get

  import arena._

  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth - 2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight - 2)

  val outsideArena: Position = Position(Point(0,0), Some(Down))
  val insideArena: Position = Position(Arena.center, Some(Down))
  val upperLeftPosition: Position = Position(Point(1, 1), Some(Down))
  
  val enemy: Enemy = Enemy(initialEnemyPosition)
  enemy.onTestingMode()

  val bullet: Bullet = Bullet(outsideArena)

  initMap()

  private def initMap(): Unit = {
    createEmptyScenario()
    player.lives = initialPlayerLife
    if(!bonusScorePoint.equals(0,0)) { collectibles = collectibles + BonusScore(Position(bonusScorePoint, None), scoreToIncrease) }
    if(!bonusLifePoint.equals(0,0)) { collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) }
    enemies = enemies + enemy
    if(!otherEnemyPoint.equals(0,0)) {
      val otherEnemy = Enemy(Position(otherEnemyPoint, Some(Up)))
      otherEnemy.onTestingMode()
      enemies = enemies + otherEnemy
    }
  }

  def createEmptyScenario(): Unit = {
    obstacles = Set()
    enemies = Set()
    collectibles = Set()
    player moveTo initialPlayerPosition
  }
}
