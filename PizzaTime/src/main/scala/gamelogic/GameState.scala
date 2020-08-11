package gamelogic

/** Represents the game's logical state. */
object GameState {
  var arena: Option[GameMap] = None
  val arenaWidth: Int = 12
  val arenaHeight: Int = 8

  def startGame(playerName: String): Unit = {
    arena = Some(Arena(playerName))
  }

  def nextStep(): Unit = ???
}

