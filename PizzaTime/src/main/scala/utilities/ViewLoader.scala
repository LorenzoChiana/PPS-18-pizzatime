package utilities

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane

object ViewLoader {
  /**
   * Loads a layout in .fxml file and assigns root and controller the passed [[Pane]]
   * @param controller the root of the .fxml file
   * @param layoutPath the file or path name
   * @return the [[Pane]] represents the control to use as view
   */
  def loadView(controller: Pane, layoutPath: String): Pane = {
    val loader = new FXMLLoader(getClass.getResource("/layouts/" + layoutPath))
    loader.setRoot(controller)
    loader.setController(controller)
    loader.load()
  }
}
