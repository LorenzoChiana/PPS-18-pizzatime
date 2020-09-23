package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Position

/** The main character.
 *
 *  @param position its [[Position]]
 *  @param lives its lives
 */
case class Hero(position: Position, lives: Int) extends LivingEntity with MovableEntity

object Hero {
  def apply(p: Position): Hero = Hero(p, difficulty.maxLife)
}