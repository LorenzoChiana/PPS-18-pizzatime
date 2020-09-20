package gameview.fx

import javafx.scene.layout.BorderPane
import utilities.ViewLoader

/** A generic view made with JavaFX.
 *  It extends [[BorderPane]]
 *  and thanks to [[ViewLoader]] it will provide to load and parse the eventually specified layout file.
 *
 *  @param layoutPath the path of layout file
 */
class FXView(val layoutPath: Option[String] = Option.empty) extends BorderPane {
  if (layoutPath.isDefined) ViewLoader.loadView(this, layoutPath.get)
}
