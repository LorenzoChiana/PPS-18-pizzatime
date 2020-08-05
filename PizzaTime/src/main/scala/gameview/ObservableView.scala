package gameview

import gameview.observer.ViewObserver

trait ObservableView[Observer <: ViewObserver] {
  protected var observers: Set[Observer] = Set.empty

  def addObserver(observer: Observer): Unit = {
    observers = observers + observer
  }
}
