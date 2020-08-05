package gameview.controller

import gameview.observer.MainSceneObserver
import gameview.scene.MainScene

/**
 * Controller of the main scene
 */
trait MainSceneController extends MainSceneObserver with Controller[MainScene]
