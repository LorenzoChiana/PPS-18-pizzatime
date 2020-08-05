package gameview.fx

import gameview.Window
import gameview.scene.MainScene
import javafx.fxml.FXML
import javafx.scene.control.Button

/**
 * Represents the implementation of the main scene that appear to the user when the application starts.
 * It permits to start game, change settings, look at credits or exit to the desktop.
 * @param windowManager the window on which the scene is applied
 */
case class FXMainScene(override val windowManager: Window) extends FXView(Some("MainScene.fxml")) with MainScene {
  @FXML protected var startGameButton: Button = _
  @FXML protected var settingsButton: Button = _
  @FXML protected var creditsButton: Button = _
  @FXML protected var exitButton: Button = _

  creditsButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.onCredits()))
}