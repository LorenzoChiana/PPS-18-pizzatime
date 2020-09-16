package gamelogic

import utilities.Position

/** A collectible placed on the [[Floor]]. */
sealed trait Collectible extends Entity

/*case class BonusExtraLife (position: Position, value: Int) extends Collectible */
case class BonusLife(position: Position) extends Collectible
case class BonusScore(position: Position, value: Int) extends Collectible
