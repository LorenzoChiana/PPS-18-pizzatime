package gamemanager.handlers

import java.util.prefs.Preferences
import net.liftweb.json._
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.{Formats, compactRender}
import net.liftweb.json.ext.EnumSerializer
import utilities.Difficulty

/** Handles the game preferences set by the user */
object PreferencesHandler {
  private val PLAYER_NAME: String = "PlayerName"
  private val DIFFICULTY: String = "Difficulty"

  private val prefs: Preferences = Preferences.userNodeForPackage(this.getClass)

  /**
   * @return the current user name saved, by default is "Player1"
   */
  def playerName: String = prefs.get(PLAYER_NAME, "Player1")

  /**
   * Sets and save the user name
   * @param name the user name
   */
  def playerName_(name: String): Unit = prefs.put(PLAYER_NAME, name)

  /** Implicit format to be able to write and read [[Difficulty]] in a json file */
  implicit val formats: Formats = net.liftweb.json.DefaultFormats + new EnumSerializer(Difficulty)

  /**
   * @return the game difficulty saved, by default it is easy
   */
  def difficulty: Difficulty.Value = {
    parse(prefs.get(DIFFICULTY, compactRender(decompose(Difficulty.Easy)))).extract[Difficulty.Value]
  }

  /**
   * Sets and save the game difficulty
   * @param difficulty the game difficulty
   */
  def difficulty_(difficulty: Difficulty.Value): Unit = {
    prefs.put(DIFFICULTY, compactRender(decompose(difficulty)))
  }

  /** Resets all preferences */
  def reset(): Unit = {
    prefs.remove(PLAYER_NAME)
    prefs.remove(DIFFICULTY)
  }
}
