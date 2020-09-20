package gamelogic

import utilities.Position

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Door]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  val position: Position
}

