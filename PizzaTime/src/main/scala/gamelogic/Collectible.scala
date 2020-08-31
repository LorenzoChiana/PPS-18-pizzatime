package gamelogic

import utilities.Position
import GameState.arena

/** A collectible placed on the [[Floor]]. */
sealed trait Collectible extends Entity

case class BonusLife(var position: Position) extends Collectible {
  override def remove(): Boolean = {
    arena.get.collectibles = arena.get.collectibles - copy()
    if (!arena.get.collectibles.contains(copy())) true else false
  }
}

case class BonusScore(var position: Position, var value: Int) extends Collectible {
  override def remove(): Boolean = {
    arena.get.collectibles = arena.get.collectibles - copy()
    if (!arena.get.collectibles.contains(copy())) true else false
  }
}