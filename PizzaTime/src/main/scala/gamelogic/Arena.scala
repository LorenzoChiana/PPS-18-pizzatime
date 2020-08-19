package gamelogic

import GameState._
import gamelogic.Arena.containsEnemy
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
            case c: BonusScore => player addScore c.value
          }
          player.collect(collectibles.find(_.position.point.equals(player.position.point)).get)
          collectibles --= collectibles.filter(_.position.point.equals(p))
        case p if containsEnemy(p) => { player.lives = player.lives -1; println(player.lives) } //forse può dare bug
        case _ => None
      }
    }

    enemies.foreach(en => {
      en.movementBehaviour()
      if (en.position.point.equals(player.position.point)) { player.lives = player.lives -1; println(player.lives) } //può generare bug con quello sopra
    })

    /**Advance the bullets*/
    bullets foreach(bullet => bullet advances)

    /**Check if any enemies are dead*/
    enemies foreach(en => if (en.lives == 0) enemies = enemies - en)
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

  /** Returns the [[Arena]]'s center point. */
  def center: Point = (arenaWidth / 2, arenaHeight / 2)

  /** Checks whether a [[Point]] is inside the [[Arena]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is inside the [[Arena]]
   */
  def checkBounds(p: Point): Boolean = p.x < arenaWidth && p.y < arenaHeight

  /** Checks whether a [[Point]] is clear or not (meaning if the [[Point]] is not occupied by any [[Entity]]).
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is clear
   */
  def isClearFloor(p: Point): Boolean = arena.get.allEntities.forall(e => !e.position.point.equals(p))

  /** Checks whether a [[Point]] contains a [[Obstacle]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains a [[Obstacle]]
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
}