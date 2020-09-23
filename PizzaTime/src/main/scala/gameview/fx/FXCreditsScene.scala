package gameview.fx

import gameview.Window
import gameview.scene.Scene
import javafx.fxml.FXML
import javafx.scene.control.Button

/** Represents the scene with credits made with JavaFX.
 *
 *  @param windowManager the window on which the scene is applied
 */
case class FXCreditsScene(override val windowManager: Window) extends FXView(Some("CreditsScene.fxml")) with Scene {
  @FXML protected var backButton: Button = _

  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.onBack()))
}
