package gameview.scene

import gamemanager.ViewObserver
import gameview.{ObservableView, Window}

trait Scene extends ObservableView[ViewObserver]{
  val windowManager: Window
}
