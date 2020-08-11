package gameview.fx

import gameview.Window
import gameview.scene.GameScene
import javafx.fxml.FXML
import javafx.scene.control.Button

case class FXCreditsGameScene(override val windowManager: Window) extends FXView(Some("CreditsScene.fxml")) with GameScene {
  @FXML protected var backButton: Button = _

  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(observer => observer.onBack()))
}
