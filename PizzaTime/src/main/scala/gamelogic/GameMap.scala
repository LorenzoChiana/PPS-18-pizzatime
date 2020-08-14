package gamelogic

/** Represents the map where the game takes place.
 *  Implemented by [[Arena]].
 */
trait GameMap {
  def mapGen: MapGenerator

  def player: Player

  var enemies: Set[EnemyCharacter]
  var bullets: Set[Bullet]
  var collectibles: Set[Collectible]
  var obstacles: Set[Obstacle]

  def walls: Set[Wall]

  def floor: Set[Floor]

  /** Returns a set of all the [[Entity]]s on the map. */
  def allEntities: Set[Entity]

  /** Generates a new level. */
  def generateMap()

  /** Updates the [[GameMap]] for the new logical step. */
  def updateMap()
}