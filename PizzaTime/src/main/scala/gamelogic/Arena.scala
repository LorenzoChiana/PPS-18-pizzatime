package gamelogic

import GameState._
import gamelogic.Arena.{bounds, center, containsBullet, containsEnemy, isDoor, tiles}
import gamemanager.SoundController.{play, stopSound}
import utilities.{BonusSound, Direction, Down, FailureSound, InjurySound, LevelMusic, LevelUp, Point, Position, ShootSound}
import utilities.ImplicitConversions._

import scala.Console.println

/** The playable area, populated with all the [[Entity]]s.
 *
 *  @param playerName the [[Player]]'s name
 *  @param mapGen the [[MapGenerator]] to use
 */
class Arena(val playerName: String, val mapGen: MapGenerator) extends GameMap {
  val player: Player = Player(playerName, Position(Arena.center, Some(Down)))
  var enemies: Set[EnemyCharacter] = Set()
  var bullets: Set[Bullet] = Set()
  var collectibles: Set[Collectible] = Set()
  var obstacles: Set[Obstacle] = Set()
  val walls: Set[Wall] = for (p <- bounds) yield Wall(Position(p, None))
  val floor: Set[Floor] = for (p <- tiles) yield Floor(Position(p, None))
  var door: Option[Point] = None
  var endedLevel: Boolean = false

  /** Returns a set of all the [[Entity]]s in the [[Arena]] that are relevant to the game.
   *  Those include: [[Enemy]]s, [[Bullet]]s, [[Collectible]]s and [[Obstacle]]s.
   */
  var allGameEntities: Set[Entity] = Set() //enemies ++ bullets ++ collectibles ++ obstacles

  /** Generates a new level. */
  def generateMap(): Unit = {mapGen.generateLevel(); play(LevelMusic)}

  private var lastInjury: Option[EnemyCharacter] = None

  /** Updates the [[Arena]] for the new logical step. */
  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit = {
    if (shoot.isDefined) {bullets = bullets + Bullet(player.position); play(ShootSound)}

    if (movement.isDefined) {
      player.move(movement.get)
      lastInjury = None

      player.position.point match {
        case p if Arena.containsCollectible(p) =>
          play(BonusSound)
          collectibles.find(_.position.point.equals(p)).get match {
            case _: BonusLife => player.increaseLife()
            case c: BonusScore => player.addScore(c.value)
          }
          collectibles = collectibles -- collectibles.filter(_.position.point.equals(p))

        case p if containsEnemy(p).nonEmpty => playerInjury(containsEnemy(p).get)

        case p if isDoor(p) =>
          endedLevel = true
          stopSound()
          emptyMap()
          generateMap() //new level

        case _ => None
      }
    }

    enemies.foreach(en => {
      val enemyHaveMove =  en.movementBehaviour
      if (lastInjury.nonEmpty) {
        if (en.equals(lastInjury.get) && enemyHaveMove) {
          lastInjury = None
        }
      }

      playerInjury(en)

      val bulletOnEnemy = containsBullet(en.position.point)

      if (bulletOnEnemy.nonEmpty) {
        en.decreaseLife
        bullets = bullets -- bulletOnEnemy
      }

      if (!player.isLive) {play(FailureSound) ; stopSound()}
    })

    def emptyMap(): Unit = {
      player.moveTo(Position(center, Some(Down)))
      enemies = Set()
      bullets = Set()
      collectibles = Set()
      obstacles = Set()
      allGameEntities = Set()
      door = None
    }

    /**Advance the bullets*/
    bullets foreach(bullet => bullet.advances())

    /**Check if any bullets are explode*/
    bullets = bullets -- bullets.filter(_.unexploded == false)

    /**Check if any enemies are dead*/
    enemies.filter(!_.isLive).foreach( en => player addScore en.pointsKilling )
    enemies = enemies -- enemies.filter(_.lives == 0)

    /**Check if door is open*/
    if(enemies.isEmpty && door.isEmpty){
      println("enemies e door empty")
      door = Some(Point(0,5))
      play(LevelUp)
    } else if (enemies.nonEmpty) door = None
  }

  def playerInjury(enemy: EnemyCharacter): Unit =
    if (containsEnemy(player.position.point).nonEmpty && lastInjury.isEmpty) {
          lastInjury = Some(enemy)
          player.decreaseLife()
          play(InjurySound)
    }
}

/** Utility methods for [[Arena]]. */
object Arena {
  /** Creates an [[Arena]].
   *
   *  @param playerName the [[Player]]'s name
   *  @param mapGen the [[MapGenerator]] to use
   *  @return the new [[Arena]] instance
   */
  def apply(playerName: String, mapGen: MapGenerator): Arena = new Arena(playerName, mapGen)

  /** Returns the set of [[Point]]s that correspond to the [[Arena]]'s [[Wall]]. */
  def bounds: Set[Point] = {
    var bounds: Set[Point] = Set()

    for (
      x <- 0 until arenaWidth;
      y <- 0 until arenaHeight
    ) bounds = bounds ++ Set[Point](
      (x, 0),
      (0, y),
      (x, arenaHeight - 1),
      (arenaWidth - 1, y)
    )
    bounds
  }

  /** Returns the set of [[Point]]s that correspond to the [[Arena]]'s [[Floor]]. */
  def tiles: Set[Point] = {
    var tiles: Set[Point] = Set()

    for (
      x <- 0 until arenaWidth;
      y <- 0 until arenaHeight
    ) tiles = tiles ++ Set[Point]((x, y))
    tiles -- bounds
  }

  /** Returns the [[Arena]]'s center [[Point]]. */
  def center: Point = (arenaWidth / 2, arenaHeight / 2)

  /** Checks whether a [[Point]] is inside the playable area of the [[Arena]] or not.
   *
   *  @param p the [[Point]] to check
   *  @param bounds to be set to true to include the [[Wall]]s
   *  @return true if the [[Point]] is inside the [[Arena]]
   */
  def checkBounds(p: Point, bounds: Boolean = false): Boolean = {
    if (bounds)
      (p.x < arenaWidth) && (p.y < arenaHeight)
    else
      (p.x > 0) && (p.y > 0) && (p.x < arenaWidth - 1) && (p.y < arenaHeight - 1)
  }

  /** Checks whether a [[Point]] is clear or not (meaning if the [[Point]] is not occupied by any [[Entity]]).
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is clear
   */
  def isClearFloor(p: Point): Boolean = arena.get.allGameEntities.forall(e => !e.position.point.equals(p))

  /** Clears a specified [[Point]] inside the [[Arena]]'s inner bounds.
   *
   *  @param p the [[Point]] to clear
   */
  def clearPoint(p: Point): Unit = {
    if (checkBounds(p) && !isClearFloor(p)) {
      arena.get.allGameEntities
        .filter(e => e.position.point.equals(p))
        .map(e => e.remove())
    }
  }

  /** Checks whether a [[Point]] contains an [[Obstacle]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains an [[Obstacle]]
   */
  def containsObstacle(p: Point): Boolean = arena.get.obstacles.exists(_.position.point.equals(p))

  /** Checks whether a [[Point]] contains a [[Collectible]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains a [[Collectible]]
   */
  def containsCollectible(p: Point): Boolean = arena.get.collectibles.exists(_.position.point.equals(p))

  /** Checks whether a [[Point]] contains a [[Enemy]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return [[EnemyCharacter]] if the [[Point]] contains a [[Enemy]]
   */
  def containsEnemy(p: Point): Option[EnemyCharacter] = arena.get.enemies.find(_.position.point.equals(p))
  
  /** Checks whether a [[Point]] contains a [[Bullet]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains a [[Bullet]]
   */
  def containsBullet(p: Point): Set[Bullet] = arena.get.bullets.filter(_.position.point.equals(p))

  /** Checks whether a [[Point]] contains the door or not.
   *
   * @param p the [[Point]] to check
   * @return true if the [[Point]] contains the door
   */
  def isDoor(p: Point): Boolean = if (arena.get.door.nonEmpty) arena.get.door.get.equals(p) else false

  /** Checks whether a [[Player]] position is the same of door position or not.
   *
   * @return true if the [[Player]] position is the same of door position
   */
  def exitLevel(): Boolean = arena.get.door.isDefined && arena.get.player.position.point.equals(Point(arena.get.door.get.x, arena.get.door.get.y))
}