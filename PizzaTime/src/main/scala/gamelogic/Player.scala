package gamelogic

import gamelogic.Arena.isDoor
import gamemanager.handlers.PreferencesHandler.difficulty
import utilities.{Point, Position}

/** The main character.
 *
 *  @param playerName its name
 *  @param position its starting position
 */
case class Player(playerName: String, var position: Position) extends MovableEntity {
  var score: Int = 0
  var lives: Int = 100

  def addScore(s: Int): Unit = score = score + s

  def increaseLife(): Unit = if (lives < difficulty.maxLife) lives = lives + 1

  def decreaseLife(): Unit = if (lives > 0) lives = lives - 1

  def isLive: Boolean = lives > 0

  override def canMove(p: Point): Boolean = super.canMove(p) || isDoor(p)
}