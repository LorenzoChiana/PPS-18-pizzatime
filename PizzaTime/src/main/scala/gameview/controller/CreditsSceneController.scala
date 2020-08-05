package gameview.controller

import gameview.observer.CreditsSceneObserver
import gameview.scene.CreditsScene

/**
 * controller of [[CreditsScene]]
 */
trait CreditsSceneController extends CreditsSceneObserver with SceneController[CreditsScene] {
  def onBack(): Unit
}
