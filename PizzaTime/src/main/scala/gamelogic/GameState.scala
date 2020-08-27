package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Direction

/** Represents the game's logical state. */
object GameState {
  var arena: Option[GameMap] = None
  var arenaWidth: Int = difficulty.arenaWidth
  var arenaHeight: Int = difficulty.arenaHeight
  var level: Int = 1
  var playerRankings: Map[String, Int] = Map()

  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
  }

  def nextStep(movement: Option[Direction], shoot: Option[Direction]): Unit = arena.get.updateMap(movement, shoot)

  def nextLevel(): Unit = {
    level = level + 1
    arena.get.generateMap()
  }

  def addRecord(playerName: String, record: Int): Unit = playerRankings += (playerName -> record)

  def checkRecord(playerName: String, record: Int): Boolean = true
  //for((key, value) <- playerRankings) yield (key, value < record)
}

