package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Position

/** The main character.
 *
 *  @param position [[Hero]] position
 *  @param lives [[Hero]] lives
 */
case class Hero(position: Position, lives: Int) extends LivingEntity

object Hero {
  def apply(p: Position): Hero = Hero(p, difficulty.maxLife)


}