package gamelogic

import gamelogic.Arena.surroundings
import utilities.Position

import scala.util.Random.nextInt

/** The [[GameMap]]'s door, used to move the [[Hero]] between the levels.
 *
 *  @param position its [[Position]]
 */
case class Door(position: Position) extends Entity

object Door {
  def exitDoor(w: Set[Wall]): Door = Door(randomDoorPosition(w))

  def entranceDoor(p: Position): Door = Door(p)

  private def randomDoorPosition(walls: Set[Wall]): Position = {
    var wall: Wall = walls.toVector(nextInt(walls.size))

    while (surroundings(wall).isEmpty) {
      wall = walls.toVector(nextInt(walls.size))
    }
    wall.position
  }
}