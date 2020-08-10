package gamelogic

import utilities.Position

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]] and [[Obstacle]].
 */
trait Entity {
  var position: Position
}
