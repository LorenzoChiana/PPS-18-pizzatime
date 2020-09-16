package gameview.fx

import gamemanager.handlers.PreferencesHandler
import gamemanager.{GameManager, ViewObserver}
import gameview.Window
import gameview.fx.FXWindow.observers
import gameview.scene.{Scene, SceneType}
import javafx.application.Platform
import javafx.scene.{Scene => JFXScene}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, ButtonType, DialogPane}
import javafx.scene.effect.BoxBlur
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.StageStyle._
import AlertType._
import utilities._
import utilities.MessageTypes._

import scala.collection.immutable

/**
 * Implementation for the [[Window]] in JavaFx.
 * @param stage the top level JavaFX container.
 */
case class FXWindow(stage: Stage) extends Window {
  private val windowContent = new BorderPane()
  private var currentScene: Option[SceneType.Value] = None

  Platform.runLater(() => {
    import javafx.stage.StageStyle
    stage.initStyle(StageStyle.UNDECORATED)
    stage.setResizable(false)

    windowContent.getStylesheets.add(getClass.getResource("/styles/MainStyle.css").toExternalForm)
    stage.getIcons.addAll(
      new Image(getClass.getResource("/images/icon/icon16x16.png").toExternalForm),
      new Image(getClass.getResource("/images/icon/icon32x32.png").toExternalForm),
      new Image(getClass.getResource("/images/icon/icon64x64.png").toExternalForm))
    stage.setScene(new JFXScene(windowContent))
    stage.setOnCloseRequest(_ => closeView())
  })

  override def showView(): Unit = {
    Platform.runLater(() => stage.show())
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
      case SceneType.PlayerRankingsScene => setClassificationScene()
      case SceneType.SettingScene => setSettingsScene()
      case SceneType.CreditsScene => setCreditsScene()
      case SceneType.GameScene => setGameScene(stage)
    }

    if (currentScene.isDefined) {
      Platform.runLater(() => {
        stage.setHeight(WindowSize.Menu.height)
        stage.setWidth(WindowSize.Menu.width)
        windowContent.setId("mainDecoratedContainer")
      })
    }

    def setMainScene(): Unit = {
      val mainScene: Scene = FXMainScene(this)
      val fxMainScene = mainScene.asInstanceOf[FXMainScene]
      GameManager.view_(mainScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxMainScene)
        Animations.Fade.fadeIn(fxMainScene)
      })
    }

    def setClassificationScene(): Unit = {
      val classificationScene: Scene = FXPlayerRankingsScene(this)
      val fxClassificationScene = classificationScene.asInstanceOf[FXPlayerRankingsScene]
      GameManager.view_(classificationScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxClassificationScene)
        Animations.Fade.fadeIn(fxClassificationScene)
      })
    }

    def setSettingsScene(): Unit = {

      val settingsScene: Scene = FXSettingsScene(this)
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
      val creditsScene: Scene = FXCreditsScene(this)
      val fxCreditsScene = creditsScene.asInstanceOf[FXCreditsScene]
      GameManager.view_(creditsScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxCreditsScene)
        Animations.Fade.fadeIn(fxCreditsScene)
      })
    }

    def setGameScene(stage: Stage): Unit = {
      observers.foreach(observer => observer.notifyStartGame())
      val gameScene: gameview.scene.Scene = FXGameScene(this, stage)
      val fxGameScene = gameScene.asInstanceOf[FXGameScene]
      GameManager.view_(gameScene)

      Platform.runLater(() => {
        windowContent.setCenter(fxGameScene)
        Animations.Fade.fadeIn(fxGameScene)
      })
    }
  }

  override def showMessage(headerText: String, message: String, messageType: MessageType): Unit = {
    showMessageHelper(Some(headerText), message, messageType)
  }

  /**
   * Implements the dialog to display.
   * @param title the title of dialog
   * @param message the message to be displayed
   * @param messageType the [[MessageType]] of the message
   */
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

  /**
   * Generates the alert depending on the type of message.
   * @param messageType the [[MessageType]] of the message
   * @return the alert
   */
  private def generateAlert(messageType: MessageType): Alert = {
    val alertPair: (String, AlertType) = messageType match {
      case Info => ("Information", INFORMATION)
      case Error => ("Error", ERROR)
      case Warning => ("Warning", WARNING)
    }

    new Alert(alertPair._2) {
      initOwner(stage)
      initStyle(TRANSPARENT)
      setTitle(alertPair._1)
      getDialogPane.getStylesheets.add(getClass.getResource("/styles/DialogStyle.css").toExternalForm)
      getDialogPane.getStyleClass.add("dialog")
    }
  }

  /**
   * Shows the alert.
   * @param alert the alert to display
   */
  private def showAlert(alert: Alert): Unit = {
    windowContent.setEffect(new BoxBlur(5, 10, 10))

    alert.showAndWait().ifPresent(_ => {
      windowContent.setEffect(new BoxBlur(0, 0, 0))
    })
  }
}

object FXWindow {
  var observers: immutable.Set[ViewObserver] = Set[ViewObserver]()

  def addObserver(obs: ViewObserver): Unit = observers = observers + obs
}