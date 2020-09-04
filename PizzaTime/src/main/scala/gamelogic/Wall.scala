package gamelogic

import gamelogic.Entity.stepPoint
import gamelogic.GameState.{arenaHeight, arenaWidth}
import utilities.{Down, Left, Point, Position, Right, Up}

/** The [[GameMap]]'s wall, situated on its bounds.
 *
 *  @param position its [[Position]]
 */
case class Wall(var position: Position) extends Entity

object Wall {

  /** Returns the set of [[Point]]s defining the [[Entity]]'s surroundings.
   *
   *  @param p the starting [[Point]]
   */
  def surrounding(p: Point): Set[Point] = {
    Set(
      stepPoint(p, Up),
      stepPoint(p, Down),
      stepPoint(p, Left),
      stepPoint(p, Right)
    ).filter(p => p.x >= 0 && p.x < arenaWidth && p.y >= 0 && p.y < arenaHeight)
  }

}