package gameview.fx

import gamemanager.handlers.PreferencesHandler
import gamemanager.{GameManager, ViewObserver}
import gameview.Window
import gameview.scene.SceneType
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, DialogPane}
import javafx.scene.effect.BoxBlur
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.{Screen, Stage, StageStyle}
import utilities._
import utilities.MessageTypes._

import scala.collection.immutable

/**
 * Implementation for the [[Window]] in JavaFx.
 * @param stage the top level JavaFX container.
 */
case class FXWindow(stage: Stage, title: String) extends Window {
  private val windowContent = new BorderPane()
  private var currentScene: Option[SceneType.Value] = None

  Platform.runLater(() => {
    stage.setResizable(false)
    stage.setTitle(title)
    windowContent.getStylesheets.add(getClass.getResource("/styles/MainStyle.css").toExternalForm)
    this.stage.getIcons.addAll(
      new Image(getClass.getResource("/images/icon/icon16x16.png").toExternalForm),
      new Image(getClass.getResource("/images/icon/icon32x32.png").toExternalForm),
      new Image(getClass.getResource("/images/icon/icon64x64.png").toExternalForm))
    stage.setScene(new Scene(windowContent))
    this.stage.setOnCloseRequest(_ => closeView())
  })

  override def showView(): Unit = {
    Platform.runLater(() => {
      centerOnScreen()
      this.stage.show()
    })
  }

  override def closeView(): Unit = {
    stage.close()
    Platform.exit()
    System.exit(0)
  }

  override def isShowing: Boolean = stage.isShowing

  override def scene: Option[SceneType.Value] = currentScene

  override def scene_(intent: Intent): Unit = {
    currentScene = Some(intent.sceneType)

    intent.sceneType match {
      case SceneType.MainScene => setMainScene()
      case SceneType.SettingScene => setSettingsScene()
      case SceneType.CreditsScene => setCreditsScene()
      case SceneType.GameScene => setGameScene()
    }

    if (currentScene.isDefined) {
      Platform.runLater(() => {
        stage.setMinHeight(WindowSize.Menu.height)
        stage.setMinWidth(WindowSize.Menu.width)
        windowContent.setId("mainDecoratedContainer")
        centerOnScreen()
      })
    }

    def setMainScene(): Unit = {
      val mainScene: gameview.scene.Scene = FXMainScene(this)
      val fxMainScene = mainScene.asInstanceOf[FXMainScene]
      GameManager.view_(mainScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxMainScene)
        Animations.Fade.fadeIn(fxMainScene)
      })
    }

    def setSettingsScene(): Unit = {
      val settingsScene: gameview.scene.Scene = FXSettingsScene(this)
      val fxSettingsScene = settingsScene.asInstanceOf[FXSettingsScene]

      GameManager.view_(settingsScene)
      fxSettingsScene.showCurrentPreferences(SettingPreferences(
        PreferencesHandler.playerName,
        PreferencesHandler.difficulty
      ))

      Platform.runLater(() => {
        windowContent.setCenter(fxSettingsScene)
        Animations.Fade.fadeIn(fxSettingsScene)
      })
    }

    def setCreditsScene(): Unit = {
      val creditsScene: gameview.scene.Scene = FXCreditsScene(this)
      val fxCreditsScene = creditsScene.asInstanceOf[FXCreditsScene]
      //val creditsSceneObserver: CreditsSceneObserver = CreditsSceneController()
      //creditsScene.addObserver(creditsSceneObserver)
      GameManager.view_(creditsScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxCreditsScene)
        Animations.Fade.fadeIn(fxCreditsScene)
      })
    }

    def setGameScene(): Unit = {
      val gameScene: gameview.scene.Scene = FXGameScene(this)
      val fxGameScene = gameScene.asInstanceOf[FXGameScene]

      GameManager.view_(gameScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxGameScene)
        Animations.Fade.fadeIn(fxGameScene)
      })
    }
  }

  override def showMessage(message: String, messageType: MessageTypes.MessageType): Unit = {
    showMessageHelper(None, message, messageType)
  }

  override def showMessage(headerText: String, message: String, messageType: MessageTypes.MessageType): Unit = {
    showMessageHelper(Some(headerText), message, messageType)
  }

  private def showMessageHelper(title: Option[String], message: String, messageType: MessageType): Unit = {
    Platform.runLater(() => {
      val alert = generateAlert(messageType)
      val dialog: DialogPane = alert.getDialogPane

      dialog.setHeaderText(title.orNull)
      dialog.setGraphic(null)
      alert.setContentText(message)

      showAlert(alert)
    })
  }

  private def generateAlert(messageType: MessageType): Alert = {
    val alertPair: (String, AlertType) = messageType match {
      case Info => ("Information", AlertType.INFORMATION)
      case Error => ("Error", AlertType.ERROR)
      case Warning => ("Warning", AlertType.WARNING)
    }

    new Alert(alertPair._2) {
      initOwner(stage)
      initStyle(StageStyle.TRANSPARENT)
      setTitle(alertPair._1)
      getDialogPane.getStylesheets.add(getClass.getResource("/styles/DialogStyle.css").toExternalForm)
      getDialogPane.getStyleClass.add("dialog")
    }
  }

  private def showAlert(alert: Alert): Unit = {
    windowContent.setEffect(new BoxBlur(5, 10, 10))

    alert.showAndWait().ifPresent(_ => {
      windowContent.setEffect(new BoxBlur(0, 0, 0))
    })
  }

  private def centerOnScreen(): Unit = {
    val primScreenBounds = Screen.getPrimary.getVisualBounds
    this.stage.setX((primScreenBounds.getWidth - this.stage.getWidth) / 2)
    this.stage.setY((primScreenBounds.getHeight - this.stage.getHeight) / 2)
  }

}

/**
 *
 */
object FXWindow {
  var observers: immutable.Set[ViewObserver] = _

  def addObserver(obs: immutable.Set[ViewObserver]): Unit = observers = obs
}