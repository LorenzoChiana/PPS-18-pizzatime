package gamelogic

import gamelogic.Entity.nearPoint
import gamelogic.GameState.startGame
import gamemanager.handlers.PreferencesHandler.difficulty_
import utilities.Difficulty.Easy
import utilities.{Direction, Down, Left, Point, Position, Right, Up}

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
  initialEnemyLife: Int = 5,
  walkableWidth: (Int, Int) = (0 ,0),
  walkableHeight: (Int, Int) = (0, 0)
) {
  difficulty_(Easy)
  startGame(playerName, MapGenerator(Easy))
  val arena: GameMap = GameState.arena.get

  import arena._

  val outsideArena: Position = Position(Point(0,0), Some(Down))
  val insideArena: Position = Position(Arena.center, Some(Down))
  val upperLeftPosition: Position = Position(Point(1, 1), Some(Down))
  
  val enemy: Enemy = Enemy(initialEnemyPosition)
  enemy.onTestingMode()

  val bullet: Bullet = Bullet(outsideArena)

  initAll()

  def createScenario1(): Unit = {
    initAll()
    collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) + BonusScore(Position(bonusScorePoint, None), 1)
  }

  def createScenario2(): Unit = {
    initAll()
    collectibles = collectibles + BonusLife(Position(nearPoint(player.position.point, Right), None))
    player.lives = initialPlayerLife
  }

  def createScenario3(): Unit = {
    initAll()
    player moveTo Position(Arena.center, Some(Right))
    collectibles = collectibles + BonusScore(Position(nearPoint(Arena.center, Right), None), scoreToIncrease)
  }

  def createScenario4(): Unit = {
    initAll()
    player moveTo Position(Arena.center, Some(Right))
    enemies = enemies + Enemy(Position(nearPoint(Arena.center, Right), Some(Left)))
    player.lives = initialPlayerLife
  }

  def createScenario5(): Unit = {
    initAll()
    player moveTo initialEnemyPosition
    enemy moveTo initialEnemyPosition
    enemy.onTestingMode()

    val enemy2 = Enemy(Position(otherEnemyPoint, Some(Right)))
    enemy2.onTestingMode()
    enemies = enemies + enemy + enemy2
  }

  def createScenario6(): Unit = {
    initAll()
    player moveTo Position(Arena.center, Some(Right))
    enemy moveTo Position(nearPoint(Arena.center, Right), Some(Right))
    enemies = enemies + enemy
    enemy.lives = initialEnemyLife
    player.lives = initialPlayerLife
  }

  private def initAll(): Unit = {
    createEmptyScenario()
    player.lives = initialPlayerLife
    if(bonusScorePoint != (0,0)) { collectibles = collectibles + BonusScore(Position(bonusScorePoint, None), scoreToIncrease) }
    if(bonusLifePoint != (0,0)) { collectibles = collectibles + BonusLife(Position(bonusLifePoint, None)) }
    enemies = enemies + enemy
    if(otherEnemyPoint != (0,0)) {
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
