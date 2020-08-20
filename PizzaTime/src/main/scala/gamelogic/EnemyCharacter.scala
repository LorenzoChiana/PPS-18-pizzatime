package gamelogic

/** Represents an enemy character.
 *  Implemented by [[Enemy]].
 */
trait EnemyCharacter extends MovableEntity {
  def lives : Int
  def movementBehaviour(): Unit
}