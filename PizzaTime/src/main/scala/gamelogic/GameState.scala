package gamelogic

import utilities.Direction

/** Represents the game's logical state. */
object GameState {
  var arena: Option[GameMap] = None
  val arenaWidth: Int = 12
  val arenaHeight: Int = 8

  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
  }

  def nextStep(movement: Option[Direction]): Unit = arena.get.updateMap(movement)

}

