package gamelogic

import GameState._
import gamelogic.Arena.{containsBullet, containsEnemy}
import utilities.{Direction, Down, Point, Position}
import utilities.ImplicitConversions._
import scala.language.postfixOps

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
  val walls: Set[Wall] = for (p <- Arena.bounds) yield Wall(Position(p, None))
  val floor: Set[Floor] = for (p <- Arena.tiles) yield Floor(Position(p, None))

  def allEntities: Set[Entity] = enemies ++ bullets ++ collectibles ++ obstacles ++ walls + player

  def generateMap(): Unit = mapGen.generateLevel()

  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit = {
    if (shoot.isDefined) bullets = bullets + Bullet(player.position)

    if (movement.isDefined) {
      player.move(movement.get)
      player.position.point match {
        case p if Arena.containsCollectible(p) =>
          collectibles.find(_.position.point.equals(p)).get match {
            case _: BonusLife => player.increaseLife()
            case c: BonusScore => player.addScore(c.value)
          }
          collectibles = collectibles -- collectibles.filter(_.position.point.equals(p))
        case p if containsEnemy(p) => player.decreaseLife()
        case _ => None
      }
    }

    enemies.foreach(en => {
      en.movementBehaviour()
      if (en.position.point.equals(player.position.point)) player.decreaseLife()

      val bulletOnEnemy = containsBullet(en.position.point)

      if (bulletOnEnemy.nonEmpty) {
        en.decreaseLife()
        bullets = bullets -- bulletOnEnemy
      }
    })

    /**Advance the bullets*/
    bullets foreach(bullet => bullet advances())

    /**Check if any enemies are dead*/
    enemies foreach(en => if (en.lives == 0) {enemies = enemies - en; player addScore en.pointsKilling})
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

  /** Checks whether a [[Point]] is inside the [[Arena]] or not.
   *
   *  @param p the [[Point]] to check
   *  @param innerBounds to be set to true to exclude the [[Wall]]s from the playable area
   *  @return true if the [[Point]] is inside the [[Arena]]
   */
  def checkBounds(p: Point, innerBounds: Boolean = false): Boolean = {
    if (innerBounds)
      (p.x > 0) && (p.y > 0) && (p.x < arenaWidth - 1) && (p.y < arenaHeight - 1)
    else
      (p.x < arenaWidth) && (p.y < arenaHeight)
  }

  /** Checks whether a [[Point]] is clear or not (meaning if the [[Point]] is not occupied by any [[Entity]]).
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is clear
   */
  def isClearFloor(p: Point): Boolean = arena.get.allEntities.forall(e => !e.position.point.equals(p))

  /** Clears a specified [[Point]] inside the [[Arena]]'s inner bounds.
   *
   *  @param p the [[Point]] to clear
   */
  def clearPoint(p: Point): Unit = if (checkBounds(p, innerBounds = true) && !isClearFloor(p)) findAndRemove(p)

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
   *  @return true if the [[Point]] contains a [[Enemy]]
   */
  def containsEnemy(p: Point): Boolean = arena.get.enemies.exists(_.position.point.equals(p))
  
  /** Checks whether a [[Point]] contains a [[Bullet]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains a [[Bullet]]
   */
  def containsBullet(p: Point): Set[Bullet] = arena.get.bullets.filter(_.position.point.equals(p))

  private def findAndRemove(p: Point): Unit = {
    arena.get.enemies.foreach(e =>
      if (e.position.point.equals(p)) arena.get.enemies = arena.get.enemies - e
    )
    arena.get.bullets.foreach(b =>
      if (b.position.point.equals(p)) arena.get.bullets = arena.get.bullets - b
    )
    arena.get.collectibles.foreach(c =>
      if (c.position.point.equals(p)) arena.get.collectibles = arena.get.collectibles - c
    )
    arena.get.obstacles.foreach(o =>
      if (o.position.point.equals(p)) arena.get.obstacles = arena.get.obstacles - o
    )
  }
}