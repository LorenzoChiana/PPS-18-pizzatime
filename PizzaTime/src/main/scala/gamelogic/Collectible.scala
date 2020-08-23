package gamelogic

import utilities.Position
import GameState.arena

/** A collectible placed on the [[Floor]]. */
sealed trait Collectible extends Entity {
  override def remove(): Boolean = {
    if (arena.get.collectibles.contains(this)) {
      arena.get.collectibles = arena.get.collectibles - this
      true
    } else false
  }
}
case class BonusLife(var position: Position) extends Collectible
case class BonusScore(var position: Position, var value: Int) extends Collectible