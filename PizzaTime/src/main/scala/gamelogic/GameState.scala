package gamelogic

import gamemanager.{GameLogicObserver, ViewObserver}
import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Direction

import scala.collection.immutable

/** Represents the game's logical state. */
object GameState {
  var arena: Option[GameMap] = None
  var arenaWidth: Int = difficulty.arenaWidth
  var arenaHeight: Int = difficulty.arenaHeight
  var playerRankings = Map.empty[String, Map[String, Int]]
  var worldRecord: Int = 0

  var observers: immutable.Set[GameLogicObserver] = Set[GameLogicObserver]()

  def addObserver(obs: GameLogicObserver): Unit = {observers = observers + obs}

  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
    observers.foreach(_.startGame())
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

