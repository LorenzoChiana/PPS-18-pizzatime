package gameview.scene

import gameview.ObservableView
import gameview.observer.CreditsSceneObserver

/**
 * Models the credits menu scene
 */
trait CreditsScene extends ObservableView[CreditsSceneObserver] with Scene
