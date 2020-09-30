package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty

/** An entity with variable lives.
 *  Implemented by [[Hero]] and [[EnemyWithRandomMove]].
 */
trait LivingEntity {
  val lives: Int

  /** Returns true if the [[LivingEntity]] is dead, false otherwise. */
  def isDead: Boolean = lives <= 0

  /** Returns true if the [[LivingEntity]] is still alive, false otherwise. */
  def isLive: Boolean = !isDead

  /** Increases the life of the [[LivingEntity]]. */
  def increaseLife(): LivingEntity = this match {
    case l: Hero => l.lives match {
      case lives if lives < difficulty.maxLife => Hero(l.position, lives + 1)
      case _ => l
    }
    case l: EnemyWithRandomMove => l.lives match {
      case lives if lives < difficulty.maxLife => EnemyWithRandomMove(l.id, l.position, lives + 1)
      case _ => l
    }
  }

  /** Decreases the life of the [[LivingEntity]]. */
  def decreaseLife(): LivingEntity = this match {
    case l: Hero => l.lives match {
      case lives if lives > 0 => Hero(l.position, lives - 1)
      case _ => l
    }
    case l: EnemyWithRandomMove => l.lives match {
      case lives if lives > 0 => EnemyWithRandomMove(l.id, l.position, lives - 1)
      case _ => l
    }
    case l: EnemyWithLeftRightMove => l.lives match {
      case lives if lives > 0 => EnemyWithLeftRightMove(l.id, l.position, lives - 1)
      case _ => l
    }
  }
}
