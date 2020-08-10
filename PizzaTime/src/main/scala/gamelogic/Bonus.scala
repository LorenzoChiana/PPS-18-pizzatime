package gamelogic

import utilities.Position

class Bonus(var position: Position) extends Collectible

object Bonus {
  def apply(position: Position): Bonus = new Bonus(position)
}