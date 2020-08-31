package gamelogic

import utilities.{Direction, Point}

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

  var door: Option[Point]
  var endedLevel: Boolean

  def walls: Set[Wall]

  def floor: Set[Floor]

  var allGameEntities: Set[Entity]

  def generateMap()

  def updateMap(movement: Option[Direction], shoot: Option[Direction])
}