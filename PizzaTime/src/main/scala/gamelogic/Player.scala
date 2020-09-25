package gamelogic

import gamelogic.GameState.playerRankings
import gamemanager.handlers.PreferencesHandler.{difficulty, playerName}

case class Player(name: String, score: Int, record: Int)

object Player {
  val initialScore: Int = 0
  val initialRecord: Int = if (playerRankings.nonEmpty && playerRankings(difficulty.toString).isDefinedAt(playerName)) {
    playerRankings(difficulty.toString)(playerName)
  } else {
    0
  }

  def apply(name: String): Player = Player(name, initialScore, initialRecord)

  def addScore(p: Player, s: Int): Player = incScore(p.score, s) match {
    case newScore if newScore >= p.record => Player(p.name, newScore, newScore)
    case newScore if newScore < p.record => Player(p.name, newScore, p.record)
  }

  private def incScore(score: Int, s: Int): Int = score + s
}