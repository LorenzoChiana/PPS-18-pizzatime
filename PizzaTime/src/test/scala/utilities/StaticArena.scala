package utilities
/*
import gamelogic.GameState.startGame
import gamelogic._
import gamemanager.handlers.PreferencesHandler.{difficulty, difficulty_}
import utilities.Difficulty.Easy

/** Builder class used to create a custom [[Arena]].
 *
 * @param playerName            the [[Player]]'s name
 * @param initialPlayerPosition the [[Player]]'s starting [[Position]]
 * @param initialPlayerLife     the [[Player]]'s initial life
 * @param initialPlayerScore    the [[Player]]'s initial score
 * @param scoreToIncrease       the score given by the score-related [[Collectible]]s
 * @param bonusLifePoint        the life-related [[Collectible]]'s [[Position]]
 * @param bonusScorePoint       the score-related [[Collectible]]'s [[Position]]
 * @param initialEnemyPosition  the [[Enemy]]'s starting [[Position]]
 * @param otherEnemyPoint       a different [[Enemy]]'s starting [[Position]]
 * @param initialEnemyLife      the [[Enemy]]'s initial life
 */
case class StaticArena(
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
  val arena: Arena = GameState.arena.get

  import arena._

  val walkableWidth: (Int, Int) = (1, difficulty.arenaWidth - 2)
  val walkableHeight: (Int, Int) = (1, difficulty.arenaHeight - 2)

  val outsideArena: Position = Position(Point(0, 0), Some(Down))
  val insideArena: Position = Position(Arena.center, Some(Down))
  val upperLeftPosition: Position = Position(Point(1, 1), Some(Down))

  val enemy: Enemy = Enemy(initialEnemyPosition)
  enemy.onTestingMode()

  val bullet: Bullet = Bullet(outsideArena)

  initMap()

  private def initMap(): Unit = {
    createEmptyScenario()
    hero.lives = initialPlayerLife

    if (!bonusScorePoint.equals(0, 0)) {
      collectibles = collectibles + BonusScore(Position(bonusScorePoint, None), scoreToIncrease)
    }
    if (!bonusLifePoint.equals(0, 0)) {
      collectibles = collectibles + BonusLife(Position(bonusLifePoint, None))
    }
    enemies = enemies + enemy
    if (!otherEnemyPoint.equals(0, 0)) {
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
} */
