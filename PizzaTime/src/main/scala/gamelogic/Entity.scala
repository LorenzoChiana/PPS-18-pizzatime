package gamelogic

import utilities.Position

trait Entity {
  val id: String
  var position: Position
}
