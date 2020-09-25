package gamelogic

import utilities.Position

/** A collectible placed on the [[Floor]]. */
sealed trait Collectible extends Entity {
  def id: Int
}

case class BonusLife(id: Int, position: Position) extends Collectible
case class BonusScore(id: Int, position: Position, value: Int) extends Collectible
