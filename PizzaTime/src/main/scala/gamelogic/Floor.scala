package gamelogic

import utilities.Position

class Floor(var position: Position) extends Entity

object Floor {
  def apply(position: Position): Floor = new Floor(position)
}