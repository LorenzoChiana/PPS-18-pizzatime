package utilities

import gamelogic.GameState.startGame
import gamelogic._
import gamemanager.handlers.PreferencesHandler.difficulty_
import utilities.Difficulty.Easy
import utilities.IdGenerator.nextId
import ImplicitConversions._

/** Builder class used to create a custom [[Arena]].
 *
 * @param playerName            the [[Player]]'s name
 * @param initialHeroPosition   the [[Hero]]'s starting [[Position]]
 * @param scoreToIncrease       the score given by the score-related [[Collectible]]s
 * @param obstaclePosition      the [[Obstacle]]'s [[Position]]
 * @param wallPosition          the [[Wall]]'s [[Position]]
 * @param bonusLifePosition     the life-related [[Collectible]]'s [[Position]]
 * @param bonusScorePosition    the score-related [[Collectible]]'s [[Position]]
 * @param initialEnemyPosition  the [[Enemy]]'s starting [[Position]]
 */
case class StaticArena(
  playerName: String = "PlayerName",
  initialHeroPosition: Position = Position(Point(0, 0), Some(Down)),
  scoreToIncrease: Int = 5,
  obstaclePosition: Position = Position(Point(0, 0), Some(Down)),
  wallPosition: Position = Position(Point(0, 0), Some(Down)),
  bonusLifePosition: Position = Position(Point(0, 0), Some(Down)),
  bonusScorePosition: Position = Position(Point(0, 0), Some(Down)),
  initialEnemyPosition: Position = Position(Point(0, 0), Some(Down))
) {

  difficulty_(Easy)
  startGame(playerName, MapGenerator(Easy))
  val arena: Arena = GameState.arena.get
  import arena._

  val enemy: Enemy = Enemy(initialEnemyPosition)
  enemy.onTestingMode()

  initMap()

  private def initMap(): Unit = {
    createEmptyScenario()

    if (!bonusScorePosition.equals(Position(Point(0, 0), Some(Down)))) {
      collectibles = collectibles + BonusScore(nextId, bonusScorePosition, scoreToIncrease)
    }
    if (!bonusLifePosition.equals(Position(Point(0, 0), Some(Down)))) {
      collectibles = collectibles + BonusLife(nextId, bonusLifePosition)
    }
    if(!obstaclePosition.equals(Position(Point(0, 0), Some(Down)))) {
      obstacles = obstacles + Obstacle(obstaclePosition)
    }
    if(!wallPosition.equals(Position(Point(0, 0), Some(Down)))) {
      walls = walls + Wall(wallPosition)
    }
    enemies = enemies + enemy
  }

  def createEmptyScenario(): Unit = {
    obstacles = Set()
    enemies = Set()
    collectibles = Set()
    hero = hero.moveTo(initialHeroPosition)
  }
}
