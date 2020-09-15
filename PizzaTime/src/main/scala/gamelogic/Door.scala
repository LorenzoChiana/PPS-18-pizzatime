package gamelogic

import gamelogic.GameState.arena
import utilities.Position

import scala.util.Random.nextInt

/** The [[GameMap]]'s door, used to move the [[Player]] between the levels.
 *
 *  @param position its [[Position]]
 */
case class Door(var position: Position) extends Entity

object Door {
  def exitDoor: Door = Door(randomDoorPosition)

  def entranceDoor(p: Position): Door = Door(p)

  private def randomDoorPosition: Position = {
    var wall: Wall = arena.get.walls.toVector(nextInt(arena.get.walls.size))

    while (wall.surroundings.isEmpty) {
      wall = arena.get.walls.toVector(nextInt(arena.get.walls.size))
    }
    wall.position
  }
}