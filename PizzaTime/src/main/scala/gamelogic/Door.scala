package gamelogic

import utilities.Position
import scala.util.Random.nextInt

/** The [[GameMap]]'s door, used to move the [[Player]] between the levels.
 *
 *  @param position its [[Position]]
 */
case class Door(var position: Position) extends Entity

object Door {
  def exitDoor(w: Set[Wall]): Door = Door(randomDoorPosition(w))

  def entranceDoor(p: Position): Door = Door(p)

  private def randomDoorPosition(walls: Set[Wall]): Position = {
    var wall: Wall = walls.toVector(nextInt(walls.size))

    while (wall.surroundings().isEmpty) {
      wall = walls.toVector(nextInt(walls.size))
    }
    wall.position
  }
}