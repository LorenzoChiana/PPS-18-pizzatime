package gamelogic

import utilities.{Point, Position}
import  Arena.isClearFloor

/** The [[GameMap]]'s wall, situated on its bounds.
 *
 *  @param position its [[Position]]
 */
case class Wall(position: Position) extends Entity {

}