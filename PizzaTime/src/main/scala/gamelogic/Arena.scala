package gamelogic

import GameState._
import Arena._
import Door._
import gamelogic.MovableEntity._
import gamelogic.Player.addScore
import utilities.{Direction, Down, Left, Point, Position, Right, Up}
import utilities.ImplicitConversions._

/** The playable area, populated with all the [[Entity]]s.
 *
 *  @param playerName the [[Hero]]'s name
 *  @param mapGen     the [[MapGenerator]] to use
 */
class Arena(val playerName: String, val mapGen: MapGenerator) extends GameMap {
  var player: Player = Player(playerName)
  var hero: LivingEntity = Hero(Position(Arena.center, Some(Down)))
  var enemies: Set[EnemyCharacter] = Set()
  var bullets: Set[Bullet] = Set()
  var collectibles: Set[Collectible] = Set()
  var obstacles: Set[Obstacle] = Set()
  var walls: Set[Wall] = for (p <- bounds) yield Wall(Position(p, None))
  val floor: Set[Floor] = for (p <- tiles) yield Floor(Position(p, None))
  var door: Option[Door] = None
  var endedLevel: Boolean = false
  private var lastInjury: Option[EnemyCharacter] = None

  /** Generates a new level. */
  def generateMap(): Unit = {
    mapGen.generateLevel()
    movePlayerOnDoor()
  }

  /** Updates the [[Arena]] for the new logical step. */
  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit = {
    checkShoot(shoot)
    checkMovement(movement)
    checkBullets()
    checkEnemies()
    checkDoor()
  }

  /** Empties the [[Arena]]. */
  def emptyMap(): Unit = {
    hero = moveTo(hero, Position(center, Some(Down))).asInstanceOf[LivingEntity]
    enemies = Set()
    bullets = Set()
    collectibles = Set()
    obstacles = Set()
  }

  private def movePlayerOnDoor(): Unit = {
    door.get.position.point match {
      case Point(0, _) => hero = moveTo(hero, Position(door.get.position.point, Some(Right))).asInstanceOf[LivingEntity]
      case Point(_, 0) => hero = moveTo(hero, Position(door.get.position.point, Some(Down))).asInstanceOf[LivingEntity]
      case Point(x, _) if x.equals(arenaWidth - 1) => hero = moveTo(hero, Position(door.get.position.point, Some(Left))).asInstanceOf[LivingEntity]
      case Point(_, y) if y.equals(arenaHeight - 1) => hero = moveTo(hero, Position(door.get.position.point, Some(Up))).asInstanceOf[LivingEntity]
    }
  }

  private def checkShoot(shoot: Option[Direction]): Unit = {
    if (shoot.isDefined && !isDoor(hero.position.point)) {
      hero = changeDirection(hero, shoot.get).asInstanceOf[LivingEntity]
      bullets = bullets + Bullet(Position(hero.position.point, shoot))
      observers.foreach(_.shoot())
    }
  }

  private def checkMovement(movement: Option[Direction]): Unit = {
    if (movement.isDefined) {
      hero = MovableEntity.move(hero, movement.get).asInstanceOf[LivingEntity]
      lastInjury = None

      hero.position.point match {
        case p if containsCollectible(p) =>
          observers.foreach(_.takesCollectible())
          collectibles.find(_.position.point.equals(p)).get match {
            case _: BonusLife => hero = hero.increaseLife()
            case c: BonusScore => player = addScore(player, c.value)
          }
          collectibles = collectibles -- collectibles.filter(_.position.point.equals(p))

        case p if containsEnemy(p).isDefined =>
          playerInjury(containsEnemy(p).get)
          observers.foreach(_.playerInjury())

        case p if isDoor(p) && enemies.isEmpty =>
          endedLevel = true
          emptyMap()
          door.get.position.point match {
            case Point(0, y) => door = Some(entranceDoor(Position(Point(arenaWidth - 1, y), None)))
            case Point(x, 0) => door = Some(entranceDoor(Position(Point(x, arenaHeight - 1), None)))
            case Point(x, y) if x.equals(arenaWidth - 1) => door = Some(entranceDoor(Position(Point(0, y), None)))
            case Point(x, y) if y.equals(arenaHeight - 1) => door = Some(entranceDoor(Position(Point(x, 0), None)))
          }
          generateMap()

        case _ => None
      }
    }
  }

  private def checkBullets(): Unit = {
    var newBullets: Set[Bullet] = Set()

    bullets.foreach(bullet => newBullets = newBullets + Bullet.move(bullet))
    bullets = bullets -- bullets.filter(!_.unexploded)

    enemies = enemiesMovement
    enemies.foreach(en => playerInjury(en))
    enemies = checkEnemiesHit

    if(hero.isDead) observers.foreach(_.playerDead())

  }

  private def checkEnemiesHit: Set[EnemyCharacter] = {
    var newEnemies: Set[EnemyCharacter] = Set()
    enemies.foreach(en => {
      val bulletOnEnemy = containsBullet(en.position.point)
      if (bulletOnEnemy.nonEmpty) {
        newEnemies = newEnemies + EnemyCharacter.decreaseLife(en)
        bullets = bullets -- bulletOnEnemy
      }
    })
    newEnemies
  }

  private def enemiesMovement: Set[EnemyCharacter] = {
    var newEnemies: Set[EnemyCharacter] = Set()
    enemies.foreach(en => en.movementBehaviour match {
      case Some(e) => if(lastInjury.isDefined && en.equals(lastInjury.get)) lastInjury = None
                      newEnemies = newEnemies + e
      case None => newEnemies = newEnemies + en
    })
    newEnemies
  }

  private def playerInjury(enemy: EnemyCharacter): Unit = {
    if (containsEnemy(hero.position.point).isDefined && lastInjury.isEmpty) {
      lastInjury = Some(enemy)
      hero = hero.decreaseLife()
      observers.foreach(_.playerInjury())
    }
  }

  private def checkEnemies(): Unit = {
    enemies.filter(_.isDead).foreach(en => player = addScore(player, en.pointsKilling))
    enemies = enemies.filter(!_.isDead)
  }

  private def checkDoor(): Unit = {
    if (door.isEmpty) {
      if (enemies.isEmpty) {
        door = Some(Door.exitDoor(walls))
        observers.foreach(_.openDoor())
        observers.foreach(_.openDoor())
      }
    } else if (enemies.nonEmpty && !hero.position.point.equals(door.get.position.point)) {
      walls = walls + Wall(Position(door.get.position.point, None))
      door = None
    }
  }

  def removeEntity(e: Entity): Unit = e match {
    case c: Collectible => collectibles = collectibles - c
    case o: Obstacle => obstacles = obstacles - o
    case e: EnemyCharacter => enemies = enemies - e
    case b: Bullet => bullets = bullets - b
    case w: Wall => walls = walls - w
    case _ =>
  }
}

/** Utility methods for [[Arena]]. */
object Arena {
  val FillRatio: Double = 0.7

  /** Creates an [[Arena]].
   *
   *  @param playerName the [[Hero]]'s name
   *  @param mapGen     the [[MapGenerator]] to use
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
    allEntities.forall(!_.position.point.equals(p) && checkBounds(p))
  }

  /** Clears a specified [[Point]] inside the [[Arena]]'s inner bounds.
   *
   *  @param p the [[Point]] to clear
   */
  def clearPoint(p: Point): Unit = {
    val allEntities: Set[Entity] = arena.get.enemies ++ arena.get.bullets ++ arena.get.collectibles ++ arena.get.obstacles
    allEntities.filter(_.position.point.equals(p)).foreach(e => arena.get.removeEntity(e))
  }

  /** Checks whether a [[Point]] is on the entrance of the [[Arena]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] is in the [[Door]]'s surroundings
   */
  def isEntrance(p: Point): Boolean = checkBounds(p) && arena.get.door.isDefined && arena.get.door.get.surroundings().contains(p)

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

  def isBonusLife(collectible: Collectible): Boolean = collectible match {
    case _: BonusLife => true
    case _ => false
  }

  def isBonusScore(collectible: Collectible): Boolean = collectible match {
    case _: BonusScore => true
    case _ => false
  }

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

  /** Checks whether a [[Point]] contains a [[Wall]] or not.
   *
   *  @param p the [[Point]] to check
   *  @return true if the [[Point]] contains a [[Wall]]
   */
  def containsWall(p: Point): Boolean = arena.get.walls.exists(_.position.point.equals(p))

  /** Checks whether a [[Point]] contains the [[Door]] or not.
   *
   * @param p the [[Point]] to check
   * @return true if the [[Point]] contains the [[Door]]
   */
  def isDoor(p: Point): Boolean = arena.get.door.isDefined && arena.get.door.get.position.point.equals(p)


  /** Checks whether a [[Hero]] is exiting the level or not.
   *
   * @return true if the [[Hero]]'s [[Position]] is the same as the [[Door]]'s
   */
  def exitLevel(): Boolean = arena.get.door.isDefined && arena.get.hero.position.point.equals(arena.get.door.get.position.point)
}