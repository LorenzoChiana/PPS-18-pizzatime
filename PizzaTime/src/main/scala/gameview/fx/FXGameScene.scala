package gameview.fx

import gameview.Window
import gameview.scene.Scene

case class FXGameScene(override val windowManager: Window) extends FXView(Some("GameScene.fxml")) with Scene {

}
