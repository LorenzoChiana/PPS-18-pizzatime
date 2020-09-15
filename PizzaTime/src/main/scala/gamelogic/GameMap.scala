package gamelogic

import utilities.Direction

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

  var walls: Set[Wall]

  def floor: Set[Floor]

  var door: Option[Door]

  var endedLevel: Boolean

  def generateMap(): Unit

  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit

  def emptyMap(): Unit
}