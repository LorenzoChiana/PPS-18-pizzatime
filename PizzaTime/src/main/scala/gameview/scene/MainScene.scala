package gameview.scene

import gameview.ObservableView
import gameview.observer.MainSceneObserver

/**
 * Models the main menu scene
 */
trait MainScene extends ObservableView[MainSceneObserver] with Scene
