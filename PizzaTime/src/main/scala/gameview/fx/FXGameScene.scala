package gameview.fx

import gamelogic.GameState.{arena, arenaHeight, arenaWidth, worldRecord}
import gamemanager.ImageLoader.images
import gameview.Window
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.Scene
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.input.KeyCode.{A, D, DOWN, LEFT, RIGHT, S, UP, W}
import javafx.scene.control.{Button, Label}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.KeyEvent
import javafx.scene.layout.{BorderPane, GridPane}
import javafx.stage.Stage
import javafx.util.Duration
import utilities.WindowSize.Game
import utilities.{Action, Down, Left, LifeBarImage0, LifeBarImage1, LifeBarImage2, LifeBarImage3, LifeBarImage4, LifeBarImage5, Movement, Point, Right, Shoot, Up}

import scala.collection.immutable.HashSet
import scala.collection.mutable

/** Represents the game scene made with JavaFX.
 *
 * @param windowManager the window on which the scene is applied
 * @param stage the top level JavaFX container
 */
case class FXGameScene(override val windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private val LifeBarHeight = 40
  private val LifeBarWight = 208

  private val actions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false,
    Action(Movement, Some(Down)) -> false,
    Action(Movement, Some(Left)) -> false,
    Action(Movement, Some(Right)) -> false,
    Action(Shoot, None) -> false,
  )

  private var elements: Set[GameElements] = HashSet(ArenaRoom(), Enemies(), Collectibles(), Bullets(), Player())

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
    case W => actions(Action(Movement, Some(Up))) = true
    case S => actions(Action(Movement, Some(Down))) = true
    case A => actions(Action(Movement, Some(Left))) = true
    case D => actions(Action(Movement, Some(Right))) = true
    case UP => actions(Action(Shoot, Some(Up))) = true
    case DOWN => actions(Action(Shoot, Some(Down))) = true
    case LEFT => actions(Action(Shoot, Some(Left))) = true
    case RIGHT => actions(Action(Shoot, Some(Right))) = true
    case _ => None
  })

  stage.getScene.setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
    case W => actions(Action(Movement, Some(Up))) = false
    case S => actions(Action(Movement, Some(Down))) = false
    case A => actions(Action(Movement, Some(Left))) = false
    case D => actions(Action(Movement, Some(Right))) = false
    case UP => actions(Action(Shoot, Some(Up))) = false
    case DOWN => actions(Action(Shoot, Some(Down))) = false
    case LEFT => actions(Action(Shoot, Some(Left))) = false
    case RIGHT => actions(Action(Shoot, Some(Right))) = false
    case _ => None
  })

  val timeline = new Timeline(new KeyFrame(Duration.millis(100), (_: ActionEvent) => {
    actions.foreach(d => if (d._2) FXWindow.observers.foreach(_.notifyAction(d._1)))
  }))
  timeline.setCycleCount(Animation.INDEFINITE)
  timeline.play()

  var lastLives: Int = arena.get.player.lives

  /** Method called by the controller cyclically to update the view */
  def updateView(): Unit = {
    elements.foreach(e => e.update())

    /** Updating player's label */
    Platform.runLater(() => {
      statsLabel.setText("Level: " + arena.get.mapGen.currentLevel +
        "   Score: " + arena.get.player.score +
        "   World record: " + worldRecord +
        "   Your record: " + arena.get.player.record)
    })

    if (!lastLives.equals(arena.get.player.lives)) {
      Platform.runLater(()=>{
        arena.get.player.lives match {
          case 5 => lifeBar.setImage(images(LifeBarImage5))
          case 4 => lifeBar.setImage(images(LifeBarImage4))
          case 3 => lifeBar.setImage(images(LifeBarImage3))
          case 2 => lifeBar.setImage(images(LifeBarImage2))
          case 1 => lifeBar.setImage(images(LifeBarImage1))
          case 0 => lifeBar.setImage(images(LifeBarImage0))
          case _ =>
        }
      })

      lastLives = arena.get.player.lives
    }
    if (arena.get.player.isDead) FXWindow.observers.foreach(_.notifyEndGame())
  }

  /** Method called by game loop when the level ended. */
  def endLevel(): Unit = {
    Platform.runLater(() => dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
  }
}

object FXGameScene {
  @FXML val dungeon: Group = new Group()
}
