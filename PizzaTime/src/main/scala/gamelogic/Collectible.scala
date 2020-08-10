package gamelogic

import utilities.Position

/** A collectible placed on the [[Floor]]. */
sealed trait Collectible extends Entity
case class BonusLife(var position: Position) extends Collectible
case class BonusScore(var position: Position, var value: Int) extends Collectible