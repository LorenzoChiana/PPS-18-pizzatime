package gameview.fx

import gameview.Window
import gameview.scene.Scene
import javafx.fxml.FXML
import javafx.scene.control.Button

case class FXCreditsScene(override val windowManager: Window) extends FXView(Some("CreditsScene.fxml")) with Scene {
  @FXML protected var backButton: Button = _

  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(observer => observer.onBack()))
}
