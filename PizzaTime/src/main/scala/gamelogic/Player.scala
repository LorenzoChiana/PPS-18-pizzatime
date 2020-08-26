package gamelogic

import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.Position

/** The main character.
 *
 *  @param playerName its name
 *  @param position its starting position
 */
case class Player(playerName: String, var position: Position) extends MovableEntity {
  var score: Int = 0
  var lives: Int = 5

  def addScore(s: Int): Unit = score = score + s

  def increaseLife(): Unit = if (lives < difficulty.maxLife) lives += 1

  def decreaseLife(): Unit = if (lives > 0) lives -= 1
}