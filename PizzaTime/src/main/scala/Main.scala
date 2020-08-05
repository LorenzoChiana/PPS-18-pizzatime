import GameManager.GameManager
import _root_.GameManager.observers.ViewObserver
import gameview.Window
import gameview.fx.FXWindow
import javafx.application.{Application, Platform}
import javafx.stage.Stage

class Main extends Application {
  override def start(primaryStage: Stage): Unit = {
    Platform.runLater(() => {
      initializeGame()
      val view: Window = FXWindow(primaryStage, "PizzaTime")
      view.showView()
    })
  }

  private def initializeGame() {
    val observers: Set[ViewObserver] = Set(new GameManager())
  }

}