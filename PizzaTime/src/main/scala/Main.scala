
import gamemanager.{GameManager, ViewObserver}
import gameview.Window
import gameview.fx.FXWindow
import gameview.scene.SceneType._
import javafx.application.Application
import javafx.stage.Stage
import utilities.Intent
import gameview.fx.FXWindow.addObserver

import scala.concurrent._
import ExecutionContext.Implicits.global

 class Main extends Application {
  def start(primaryStage: Stage): Unit = {
    initializeGame(primaryStage)
  }

  private def initializeGame(primaryStage: Stage): Future[Unit] = Future {
    val view: Window = FXWindow(primaryStage, "PizzaTime")
    val observers: Set[ViewObserver] = Set(GameManager)
    addObserver(observers)
    view.scene_(new Intent(MainScene))
    view.showView()
  }
}