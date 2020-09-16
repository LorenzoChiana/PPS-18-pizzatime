package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty

trait LivingEntity extends MovableEntity {
  val lives: Int

  def isDead: Boolean = lives <= 0

  def isLive: Boolean = !isDead

  def increaseLife(): LivingEntity = this match {
    case l: Hero => l.lives match {
      case lives if lives < difficulty.maxLife => Hero(l.position, lives + 1)
      case _ => l
    }
    case l: Enemy => l.lives match {
      case lives if lives < difficulty.maxLife => Enemy(l.position, lives + 1)
      case _ => l
    }
  }

  def decreaseLife(): LivingEntity = this match {
    case l: Hero => l.lives match {
      case lives if lives > 0 => Hero(l.position, lives - 1)
      case _ => l
    }
    case l: Enemy => l.lives match {
      case lives if lives > 0 => Enemy(l.position, lives - 1)
      case _ => l
    }
  }
}
