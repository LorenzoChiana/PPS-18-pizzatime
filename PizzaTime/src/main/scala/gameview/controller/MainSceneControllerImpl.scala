package gameview.controller

import gameview.scene.SceneType
import utilities.Intent

case class MainSceneControllerImpl() extends MainSceneController {
  override def onStartGame(): Unit = {
  require(view.isDefined)
  view.get.windowManager.scene_(new Intent(SceneType.GameScene))
}

  override def onSettings(): Unit = {
  require(view.isDefined)
  view.get.windowManager.scene_(new Intent(SceneType.SettingScene))
}
}
