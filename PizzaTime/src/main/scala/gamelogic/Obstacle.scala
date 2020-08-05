package gamelogic

import utilities.Position

class Obstacle(var position: Position) extends Entity

object Obstacle {
  def apply(position: Position): Obstacle = new Obstacle(position)
}