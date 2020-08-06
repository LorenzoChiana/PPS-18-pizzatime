import gamemanager.GameManager
import gamemanager.observers.ViewObserver
import gameview.Window
import gameview.fx.FXWindow
import gameview.scene.SceneType
import javafx.application.{Application, Platform}
import javafx.stage.Stage
import utilities.Intent

class Main extends Application {
  override def start(primaryStage: Stage): Unit = {
    initializeGame(primaryStage)
  }

  private def initializeGame(primaryStage: Stage) {
    Platform.runLater(() => {
      val view: Window = FXWindow(primaryStage, "PizzaTime")
      val observers: Set[ViewObserver] = Set(new GameManager(view))

      view.scene_(new Intent(SceneType.MainScene))
      view.showView()
    })
  }

}