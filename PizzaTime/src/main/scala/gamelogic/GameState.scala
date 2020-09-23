package gamelogic

import gamemanager.GameLogicObserver
import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Direction

/** Represents the game's logical state. */
object GameState {
  var arena: Option[Arena] = None
  var arenaWidth: Int = difficulty.arenaWidth
  var arenaHeight: Int = difficulty.arenaHeight
  var playerRankings: Map[String, Map[String, Int]] = Map.empty[String, Map[String, Int]]
  var worldRecord: Int = 0
  var observers: Set[GameLogicObserver] = Set[GameLogicObserver]()

  def addObserver(obs: GameLogicObserver): Unit = {
    observers = observers + obs
  }

  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
    observers.foreach(_.startGame())
  }

  def endGame(): Unit = addRecord()

  def checkNewWorldRecord(): Option[Int] = arena.get.player.record match {
    case r if r > worldRecord => Some(arena.get.player.record)
    case _ => None
  }

  def nextStep(movement: Option[Direction], shoot: Option[Direction]): Unit = arena.get.updateMap(movement, shoot)

  def nextLevel(): Unit = arena.get.generateMap()

  def addRecord(): Unit = {
    playerRankings = playerRankings ++ Map(difficulty.toString -> (
        playerRankings(difficulty.toString) ++ Map(arena.get.player.name -> arena.get.player.record)))
  }
}

