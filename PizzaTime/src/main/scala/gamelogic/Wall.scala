package gamelogic

import utilities.Position

class Wall(var position: Position) extends Entity

object Wall {
  def apply(position: Position): Wall = new Wall(position)
}
