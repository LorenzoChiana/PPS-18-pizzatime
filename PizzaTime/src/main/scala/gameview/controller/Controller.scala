package gameview.controller

/**
 * Controls the view and its scenes
 * @tparam View the view
 */
trait Controller[View] {
  protected var view: Option[View] = None
  def setView(view: View): Unit = this.view = Some(view)
}
