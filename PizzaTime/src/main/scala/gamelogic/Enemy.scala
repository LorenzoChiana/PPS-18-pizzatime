package gamelogic

import utilities.Position

/** An enemy character.
 *
 *  @param position its starting position
 *  @param id a unique number for reference
 */
case class Enemy(var position: Position, var id: Int) extends EnemyCharacter {

}