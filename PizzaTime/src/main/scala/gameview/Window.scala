package gameview

import gameview.scene.SceneType
import utilities.Intent
import utilities.MessageTypes.MessageType

/** A generic window */
trait Window {

  /** Allows you to show the window */
  def showView(): Unit

  /** Allows you to close the window */
  def closeView(): Unit

  /**
   * Check if the window is visible
   * @return true if window is visible, false if window is not visible
   */
  def isShowing: Boolean

  /**
   * Gets the current scene inside of the window
   * @return a [[SceneType.Value]] if there is a visible scene, [[None]] if there is not a visible scene
   */
  def scene: Option[SceneType.Value]

  /**
   * Sets the scene inside of the window
   * @param intent the intent to change current scene into another
   */
  def scene_(intent: Intent): Unit

  /**
   * Shows a message to the user with a title
   * @param headerText the title of dialog
   * @param message the message to be displayed
   * @param messageType the [[MessageType]] of the message
   */
  def showMessage(headerText: String, message: String, messageType: MessageType): Unit
}
