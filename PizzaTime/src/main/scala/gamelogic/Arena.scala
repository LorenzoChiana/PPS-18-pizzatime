package gamelogic

import GameState._
import gamelogic.Arena.{bounds, center, containsBullet, containsEnemy, isDoor, tiles}
import gamemanager.SoundController.{play, stopSound}
import utilities.{BonusSound, Direction, Down, FailureSound, InjurySound, LevelMusic, LevelUp, Point, Position, ShootSound}
import utilities.ImplicitConversions._

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
  var door: Option[Door] = None
  var endedLevel: Boolean = false
  private var lastInjury: Option[EnemyCharacter] = None

  /** Generates a new level. */
  def generateMap(): Unit = {
    mapGen.generateLevel()
   /* play(LevelMusic)*/
  }

  /** Updates the [[Arena]] for the new logical step. */
  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit = {
    if (shoot.isDefined) {
      player.changeDirection(shoot.get)
      bullets = bullets + Bullet(Position(player.position.point, shoot))
      play(ShootSound)
    }

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
          generateMap()

        case _ => None
      }
    }

    /**Advance the bullets*/
    bullets foreach(bullet => bullet.advances())

    /**Check if any bullets are explode*/
    bullets = bullets -- bullets.filter(_.unexploded == false)

    enemies.foreach(en => {
      val enemyHaveMove = en.movementBehaviour

      if (lastInjury.nonEmpty) {
        if (en.equals(lastInjury.get) && enemyHaveMove) {
          lastInjury = None
        }
      }

      playerInjury(en)

      val bulletOnEnemy = containsBullet(en.position.point)

      if (bulletOnEnemy.nonEmpty) {
        en.decreaseLife()
        bullets = bullets -- bulletOnEnemy
      }

      if (player.isDead) {
        play(FailureSound)
        stopSound()
      }
    })

    /**Check if any enemies are dead*/
    enemies.filter(_.isDead).foreach(en => player.addScore(en.pointsKilling))
    enemies = enemies -- enemies.filter(_.isDead)

    /**Check if [[Door]] is open*/
    if (enemies.isEmpty && door.isEmpty) {
      door = Some(Door(Position(Point(0, 5), Some(Down))))
      play(LevelUp)
    } else if (enemies.nonEmpty) {
      door = None
    }
  }

  /** Empties the [[Arena]]. */
  def emptyMap(): Unit = {
    player.moveTo(Position(center, Some(Down)))
    enemies = Set()
    bullets = Set()
    collectibles = Set()
    obstacles = Set()
    door = None
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
  val FillRatio: Double = 0.7

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

  /** Checks whether the [[Arena]] is too full or not.
   *
   *  @return true if the [[Arena]] can be populated with [[Entity]]s
   */
  def checkArenaPopulation(fillRatio: Double = FillRatio): Boolean = {
    val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.collectibles ++ arena.get.obstacles
    allEntities.size < (arena.get.floor.size * fillRatio)
  }

  /** Checks whether a [[Point]] is clear or not (meaning if it's not occupied by any [[Entity]]).
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is clear
   */
  def isClearFloor(p: Point): Boolean = {
    val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.bullets ++ arena.get.collectibles ++ arena.get.obstacles
    allEntities.forall(!_.position.point.equals(p))
  }

  /** Clears a specified [[Point]] inside the [[Arena]]'s inner bounds.
   *
   *  @param p the [[Point]] to clear
   */
  def clearPoint(p: Point): Unit = {
    if (checkBounds(p) && !isClearFloor(p)) {
      val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.bullets ++ arena.get.collectibles ++ arena.get.obstacles
      allEntities.filter(_.position.point.equals(p)).map(_.remove())
    }
  }

  /** Checks whether a [[Point]] is on the entrance of the [[Arena]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is in the [[Door]]'s surroundings
   */
  def isEntrance(p: Point): Boolean = arena.get.door.isDefined && arena.get.door.get.surroundings.contains(p)

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

  /** Checks whether a [[Point]] contains the [[Door]] or not.
   *
   * @param p the [[Point]] to check
   * @return true if the [[Point]] contains the [[Door]]
   */
  def isDoor(p: Point): Boolean = if (arena.get.door.isDefined) arena.get.door.get.position.point.equals(p) else false

  /** Checks whether a [[Player]] is exiting the level or not.
   *
   * @return true if the [[Player]]'s [[Position]] is the same as the [[Door]]'s
   */
  def exitLevel(): Boolean = arena.get.door.isDefined && arena.get.player.position.point.equals(arena.get.door.get.position.point)
}