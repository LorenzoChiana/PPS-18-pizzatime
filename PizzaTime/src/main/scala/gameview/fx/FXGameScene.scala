package gameview.fx

import gameview.Window
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.Scene
import gameview.scene.SceneType.MainScene
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.scene.{Group, Scene => JFXScene}
import javafx.scene.input.KeyCode.{DOWN, LEFT, RIGHT, SPACE, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Action, Down, Intent, Left, Movement, Point, Right, Shoot, Up}
import javafx.scene.control.Label

import scala.collection.immutable.HashSet
import scala.collection.mutable

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private val actions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false,
    Action(Movement, Some(Down)) -> false,
    Action(Movement, Some(Left)) -> false,
    Action(Movement, Some(Right)) -> false,
    Action(Shoot, None) -> false,
  )

  private var elements: Set[GameElements] = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())

  private var userStatsLabel: Label = _

  val scene: JFXScene = new JFXScene(dungeon) {
    setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
      case UP => actions(Action(Movement, Some(Up))) = true
      case DOWN => actions(Action(Movement, Some(Down))) = true
      case LEFT => actions(Action(Movement, Some(Left))) = true
      case RIGHT => actions(Action(Movement, Some(Right))) = true
      case SPACE => actions(Action(Shoot, None)) = true
      case _ => None
    })

    setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
      case UP => actions(Action(Movement, Some(Up))) = false
      case DOWN => actions(Action(Movement, Some(Down))) = false
      case LEFT => actions(Action(Movement, Some(Left))) = false
      case RIGHT => actions(Action(Movement, Some(Right))) = false
      case SPACE => actions(Action(Shoot, None)) = false
      case _ => None
    })
  }

  createLabelStats()
  Platform.runLater(() => {
    dungeon.getChildren.add(userStatsLabel)
    stage.setScene(scene)
    stage.show()
  })

  val timeline = new Timeline(new KeyFrame(Duration.millis(80), (_: ActionEvent) => {
    actions.foreach(d => if (d._2) FXWindow.observers.foreach(o => o.notifyAction(d._1)))
  }))
  timeline.setCycleCount(Animation.INDEFINITE)
  timeline.play()

  /**
   * method called by the controller cyclically to update the view
   */
  def updateView(): Unit = {
    elements.foreach(e => e.update())
    /** Updating player lives */
    Platform.runLater(() => userStatsLabel.setText(
      PreferencesHandler.playerName + ": " + arena.get.player.lives + " \u2764 " +
        " Score: " + arena.get.player.score +
        " Record: " + arena.get.player.record))
    if (!arena.get.player.isLive) showAlertMessage()
  }

  def showAlertMessage(): Unit = {
    Platform.runLater(()=>{
      import javafx.scene.control.Alert
      import javafx.scene.control.ButtonType

      val alert = new Alert(Alert.AlertType.NONE, "You lose", ButtonType.CLOSE)
      alert.setTitle("GAME OVER!")

      alert.showAndWait()
          .filter(response => response == ButtonType.CLOSE)
          .ifPresent(_ => FXWindow.observers.foreach(observer => observer.onBack()))
    })
  }

  def endLevel(): Unit = {
    Platform.runLater(() =>dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
    Platform.runLater(() => {
      dungeon.getChildren.add(userStatsLabel)
    })
  }

  private def createLabelStats(): Unit = {
    userStatsLabel = new Label(
      PreferencesHandler.playerName + ": " + arena.get.player.lives +
      " Score: " + arena.get.player.score +
      " Record: " + arena.get.player.record
    )
    userStatsLabel.setStyle("-fx-font-style: italic; -fx-font-size: 40; -fx-text-fill: #92de34; -fx-font-weight: bold; -fx-background-color: #4444; " +
      "-fx-padding: 8px; -fx-background-radius: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);")
    userStatsLabel.relocate(Game.width / 3.5, 1)
  }
}

/** Utility methods for [[FXGameScene]]. */
object FXGameScene {
  val dungeon: Group = new Group()
  /**
   * Defines the width of each tile that will make up the arena
   *
   * @return the width of the tile
   */
  def tileWidth: Double = Game.width / arenaWidth

  /**
   * Defines the height of each tile that will make up the arena
   *
   * @return the height of the tile
   */
  def tileHeight: Double = Game.height / arenaHeight

  /** Converts a logic [[Point]] to a pixel for visualization purposes.
   *
   *  @param p the [[Point]] to convert
   *  @return a tuple of coordinates in pixels
   */
  def pointToPixel(p: Point): (Double, Double) = (p.x * tileWidth, p.y * tileHeight)

  /**
   * Creates a tile sprite
   * @param image the sprite image
   * @return an [[ImageView]] that represents the sprite of the tile
   */
  def createTile(image: Image): ImageView = {
    val tile: ImageView = new ImageView(image)
    tile.setFitWidth(tileWidth)
    tile.setFitHeight(tileHeight)
    tile
  }
}
