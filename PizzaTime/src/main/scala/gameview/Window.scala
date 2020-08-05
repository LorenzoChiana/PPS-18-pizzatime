package gameview

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
   * Shows a message to the user without a title
   * @param message the message to be displayed
   * @param messageType the [[MessageType]] of the message
   */
  def showMessage(message: String, messageType: MessageType): Unit

  /**
   * Shows a message to the user with a title
   * @param headerText the title of dialog
   * @param message the message to be displayed
   * @param messageType the [[MessageType]] of the message
   */
  def showMessage(headerText: String, message: String, messageType: MessageType): Unit
}
