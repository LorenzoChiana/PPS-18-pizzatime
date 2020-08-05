package gameview.controller

import gameview.scene.SceneType
import utilities.Intent

case class CreditsSceneControllerImpl() extends CreditsSceneController {
  override def onBack(): Unit = {
    require(view.isDefined)
    view.get.windowManager.scene_(new Intent(SceneType.MainScene))
  }
}
