package gameview.fx

import gamelogic.GameState
import gameview.Window
import gameview.scene.Scene
import javafx.fxml.FXML
import javafx.geometry.Pos._
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, VBox}

/**
 * Represents the scene with the record classification
 *
 * @param windowManager the window on which the scene is applied
 */
case class FXPlayerRankingsScene(override val windowManager: Window) extends FXView(Some("PlayerRankingsScene.fxml")) with Scene {
  @FXML protected var recordsContainer: VBox = _
  @FXML protected var backButton: Button = _
  private val recordsList: Map[String, Map[String, Int]] = GameState.playerRankings

  recordsList foreach {
    case (name, record) => {
      val hBox = new HBox()
      hBox.setSpacing(20)
      hBox.setAlignment(CENTER)
      hBox.getChildren.add(new Label(name + ": " + record))
      recordsContainer.getChildren.add(hBox)
    }
  }


  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(observer => observer.onBack()))
}