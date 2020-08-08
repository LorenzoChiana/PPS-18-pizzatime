package gameview

import gamemanager.ViewObserver

/**
 * A view observed by a [[ViewObserver]]
 * @tparam Observer the observer that observe this view
 */
trait ObservableView[Observer <: ViewObserver] {
  protected var observers: Set[Observer] = Set.empty

  /**
   * Add an observer to the view
   * @param observer the [[Observer]] that observe the view
   */
  def addObserver(observer: Observer): Unit = {
    observers = observers + observer
  }
}
