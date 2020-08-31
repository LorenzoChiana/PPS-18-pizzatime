package gamelogic

import utilities.{Direction, Point}

/** Represents the map where the game takes place.
 *  Implemented by [[Arena]].
 */
trait GameMap {
  def player: Player

  def mapGen: MapGenerator

  var enemies: Set[EnemyCharacter]
  var bullets: Set[Bullet]
  var collectibles: Set[Collectible]
  var obstacles: Set[Obstacle]
  var door: Option[Point]
  var endedLevel: Boolean

  def walls: Set[Wall]

  def floor: Set[Floor]

  def generateMap(): Unit

  def updateMap(movement: Option[Direction], shoot: Option[Direction]): Unit

  def emptyMap(): Unit
}