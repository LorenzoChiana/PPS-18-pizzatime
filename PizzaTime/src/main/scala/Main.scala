
import gamemanager.GameManager.initializeGame
import javafx.application.Application
import javafx.stage.Stage

class Main extends Application {
  def start(primaryStage: Stage): Unit = {
    initializeGame(primaryStage)
  }

}