package gamelogic

import utilities.{Down, Left, Point, Position, Right, Up}

import utilities.Position.changePosition

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Door]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  val position: Position
}

