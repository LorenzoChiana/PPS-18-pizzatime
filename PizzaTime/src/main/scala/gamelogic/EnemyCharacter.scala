package gamelogic

import utilities.IdGenerator.nextId

/** Represents an enemy character.
 *  Implemented by [[Enemy]].
 */
trait EnemyCharacter extends LivingEntity with MovableEntity {

  def id: Int
  def pointsKilling: Int
  def movementBehaviour: Option[EnemyCharacter]

}
