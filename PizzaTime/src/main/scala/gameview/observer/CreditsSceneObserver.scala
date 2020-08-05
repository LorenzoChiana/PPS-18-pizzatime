package gameview.observer

/**
 * observer of [[CreditsScene]]
 */
trait CreditsSceneObserver extends ViewObserver {
  def onBack(): Unit
}
