package gamelogic

/** Represents an enemy character.
 *  Implemented by [[Enemy]].
 */
trait EnemyCharacter extends LivingEntity {

  def pointsKilling: Int
  def movementBehaviour: Option[EnemyCharacter]

}

object EnemyCharacter {
  def decreaseLife(e: EnemyCharacter): Enemy = e.lives match {
    case l if l > 0 => Enemy(e.position, l -1)
    case _ => Enemy(e.position, 0)
  }
}