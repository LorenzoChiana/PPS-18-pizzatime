package gamelogic

import utilities.Position

/** Represents a basic entity, defined by a [[Position]].
 *  Implemented by [[Wall]], [[Floor]], [[Obstacle]], [[Collectible]] and [[MovableEntity]].
 */
trait Entity {
  var position: Position

  /** Removes the [[Entity]] from the [[Arena]].
   *
   *  @return true if the [[Entity]] is present in the [[Arena]] and can be removed, false otherwise
   */
  def remove(): Boolean = false
}
