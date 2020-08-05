package gameview.fx

import gameview.Window
import gameview.scene.CreditsScene
import javafx.fxml.FXML
import javafx.scene.control.Button

case class FXCreditsScene (override val windowManager: Window) extends FXView(Some("CreditsScene.fxml")) with CreditsScene {
  @FXML protected var backButton: Button = _

  backButton.setOnMouseClicked(_ => observers.foreach(observer => observer.onBack()))
}
