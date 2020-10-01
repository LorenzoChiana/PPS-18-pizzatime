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

  /** Permits to add an observer to the observer set
   *
   *  @param obs the observer to add
   */
  def addObserver(obs: GameLogicObserver): Unit = {
    observers = observers + obs
  }

  /** The actions to do at the beginning of the game
   *
   *  @param playerName the name of the player
   *  @param mapGen the new map
   */
  def startGame(playerName: String, mapGen: MapGenerator): Unit = {
    arena = Some(Arena(playerName, mapGen))
    arena.get.generateMap()
    observers.foreach(_.startGame())
  }

  /** The actions to do at the end of the game */
  def endGame(): Unit = arena.get.addRecord()

  /** Check if the player has made a new global record */
  def checkNewWorldRecord(): Option[Int] = arena.get.player.record match {
    case r if r > worldRecord => Some(arena.get.player.record)
    case _ => None
  }

  /** Refresh the arena map
   *
   *  @param movement any movement
   *  @param shoot any shot
   */
  def nextStep(movement: Option[Direction], shoot: Option[Direction]): Unit = arena.get.updateMap(movement, shoot)

  /** Generates a new level */
  def nextLevel(): Unit = arena.get.generateMap()
}

