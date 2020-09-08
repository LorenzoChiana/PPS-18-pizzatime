package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Direction

/** Represents the game's logical state. */
object GameState {
  var arena: Option[GameMap] = None
  var arenaWidth: Int = difficulty.arenaWidth
  var arenaHeight: Int = difficulty.arenaHeight
  var playerRankings = Map.empty[String, Map[String, Int]]
  var worldRecord: Int = 0


  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
  }

  def endGame(): Unit = {
    arena.get.player.checkNewOwnRecord()
    addRecord()
  }

  def nextStep(movement: Option[Direction], shoot: Option[Direction]): Unit = arena.get.updateMap(movement, shoot)

  def nextLevel(): Unit = {
    arena.get.generateMap()
  }

  def addRecord(): Unit =
    playerRankings = playerRankings ++ Map(difficulty.toString -> (
      playerRankings(difficulty.toString) ++ Map(arena.get.player.playerName -> arena.get.player.record)))
}

