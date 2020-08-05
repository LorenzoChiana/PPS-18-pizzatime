package gamelogic

import utilities.Position

class Bullet(var position: Position) extends MovableEntity

object Bullet {
  def apply(position: Position): Bullet = new Bullet(position)
}