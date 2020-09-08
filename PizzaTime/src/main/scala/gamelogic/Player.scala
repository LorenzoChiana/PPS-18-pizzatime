package gamelogic

import gamelogic.GameState.{playerRankings, worldRecord}
import gamelogic.Arena.isDoor
import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.{Point, Position}
import utilities.ImplicitConversions._

/** The main character.
 *
 *  @param playerName its name
 *  @param position its starting position
 */
case class Player(playerName: String, var position: Position) extends MovableEntity {
  var score: Int = 0
  var lives: Int = 5
  var record: Int = if (playerRankings.nonEmpty && playerRankings(difficulty).isDefinedAt(playerName))
                      playerRankings(difficulty)(playerName)
                    else
                      0

  def addScore(s: Int): Unit = {
    score += s
    checkNewOwnRecord()
    checkNewWorldRecord()
  }

  def checkNewOwnRecord(): Unit = if (score > record) record = score

  def checkNewWorldRecord(): Unit = if (record > worldRecord) worldRecord = record

  def increaseLife(): Unit = if (lives < difficulty.maxLife) lives += 1

  def decreaseLife(): Unit = if (lives > 0) lives -= 1

  def isLive: Boolean = lives > 0

  def isDead: Boolean = !isLive

  override def canMoveIn(p: Point): Boolean = super.canMoveIn(p) || isDoor(p)
}