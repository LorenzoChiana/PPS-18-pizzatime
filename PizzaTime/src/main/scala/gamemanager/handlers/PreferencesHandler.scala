package gamemanager.handlers

import java.util.prefs.Preferences

import net.liftweb.json._
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.{Formats, compactRender}
import net.liftweb.json.ext.EnumSerializer
import utilities.Difficulty

object PreferencesHandler {
  private val PLAYER_NAME: String = "PlayerName"
  private val DIFFICULTY: String = "Difficulty"

  private val prefs: Preferences = Preferences.userNodeForPackage(this.getClass)

  def setName(name: String): Unit = prefs.put(PLAYER_NAME, name)
  def getName: String = prefs.get(PLAYER_NAME, "Player1")

  implicit val formats: Formats = net.liftweb.json.DefaultFormats + new EnumSerializer(Difficulty)

  def setDifficulty(difficulty: Difficulty.Value): Unit = {
    prefs.put(DIFFICULTY, compactRender(decompose(difficulty)))
  }

  def getDifficulty: Difficulty.Value = {
    parse(prefs.get(DIFFICULTY, compactRender(decompose(Difficulty.Easy)))).extract[Difficulty.Value]
  }

  def reset(): Unit = {
    prefs.remove(PLAYER_NAME)
    prefs.remove(DIFFICULTY)
  }
}
