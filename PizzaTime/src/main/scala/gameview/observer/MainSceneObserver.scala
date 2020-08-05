package gameview.observer

/**
 * Observer of [[MainScene]]
 */
trait MainSceneObserver extends ViewObserver {
  def onStartGame(): Unit
  def onSettings(): Unit
  def onCredits(): Unit
  def onExit(): Unit
}
