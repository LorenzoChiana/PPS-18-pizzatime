package gameview.fx

import gamelogic.GameState.{arena, worldRecord}
import gamemanager.GameManager.windowManager.showMessage
import gamemanager.ImageLoader.images
import gameview.Window
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.Scene
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.input.KeyCode.{A, D, DOWN, LEFT, RIGHT, S, UP, W}
import javafx.scene.control.{Button, Label}
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent
import javafx.scene.layout.{BorderPane, GridPane}
import javafx.stage.Stage
import utilities.{Action, Down, Left, LifeBarImage0, LifeBarImage1, LifeBarImage2, LifeBarImage3, LifeBarImage4, LifeBarImage5, MessageTypes, Movement, Right, Shoot, Up}

import scala.annotation.elidable.WARNING
import scala.collection.immutable
import scala.collection.immutable.HashSet

/** Represents the game scene made with JavaFX.
 *
 * @param windowManager the window on which the scene is applied
 * @param stage the top level JavaFX container
 */
case class FXGameScene(override val windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private val LifeBarHeight = 40
  private val LifeBarWight = 208

  private var elements: immutable.Set[GameElements] = HashSet(ArenaRoom(), Enemies(), Collectibles(), Bullets(), Player())

  @FXML protected var root: GridPane = _
  @FXML protected var statsPane: BorderPane = _
  @FXML protected var statsLabel: Label = _
  @FXML protected var endButton: Button = _
  @FXML protected var lifeBar: ImageView = _

  Platform.runLater(()=> {
    lifeBar.setImage(images(LifeBarImage5))
    lifeBar setFitHeight LifeBarHeight
    lifeBar setFitWidth LifeBarWight
  })

  root.add(dungeon, 0, 1)

  endButton.setOnMouseClicked(_ => FXWindow.observers.foreach(obs => {
    obs.notifyEndGame()
    obs.onBack()
  }))


  stage.getScene.setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
    case W => eventOccurred(Action(Movement, Some(Up)))
    case S => eventOccurred(Action(Movement, Some(Down)))
    case A => eventOccurred(Action(Movement, Some(Left)))
    case D => eventOccurred(Action(Movement, Some(Right)))
    case UP => eventOccurred(Action(Shoot, Some(Up)))
    case DOWN => eventOccurred(Action(Shoot, Some(Down)))
    case LEFT => eventOccurred(Action(Shoot, Some(Left)))
    case RIGHT => eventOccurred(Action(Shoot, Some(Right)))
    case _ => None
  })

  /**
   * Method called by the controller cyclically to update the view
   */
  def updateView(): Unit = arena.get.player.isDead match {
    case false =>
      elements.foreach(e => e.update())
      updateStatsLabel()
      updateLifeBar()

    case true =>
      FXWindow.observers.foreach(_.notifyEndGame())
      showMessage("OH NO!", "You Lose", MessageTypes.Warning)
  }

  /** Method called by controller when the level ended. */
  def endLevel(): Unit = {
    Platform.runLater(() => dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
  }

  /**
   * Call when [[KeyEvent]] occured
   * @param event the [[Action]] to be notified to the observers
   */
  private def eventOccurred(event: Action): Unit =  FXWindow.observers.foreach(_.notifyAction(event))

  /**
   *  Updating player's label
   */
  private def updateStatsLabel(): Unit =
    Platform.runLater(() => {
      statsLabel.setText("Level: " + arena.get.mapGen.currentLevel +
        "   Score: " + arena.get.player.score +
        "   World record: " + worldRecord +
        "   Your record: " + arena.get.player.record)
    })

  /**
   * Updating lifeBar
   */
  var lastLives: Int = arena.get.player.lives
  private def updateLifeBar(): Unit =
    if (!lastLives.equals(arena.get.player.lives)) {
      Platform.runLater(() => arena.get.player.lives match {
        case 5 => lifeBar.setImage(images(LifeBarImage5))
        case 4 => lifeBar.setImage(images(LifeBarImage4))
        case 3 => lifeBar.setImage(images(LifeBarImage3))
        case 2 => lifeBar.setImage(images(LifeBarImage2))
        case 1 => lifeBar.setImage(images(LifeBarImage1))
        case 0 => lifeBar.setImage(images(LifeBarImage0))
        case _ =>
      })
      lastLives = arena.get.player.lives
    }
}

object FXGameScene {
  @FXML val dungeon: Group = new Group()
}
