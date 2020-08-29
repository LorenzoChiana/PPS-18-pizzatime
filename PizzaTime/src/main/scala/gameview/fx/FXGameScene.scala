package gameview.fx

import gameview.Window
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gamemanager.GameManager.view
import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.GameScene
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.scene.{Group, Scene}
import javafx.scene.input.KeyCode.{DOWN, LEFT, RIGHT, SPACE, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Action, Down, Left, Movement, Point, Right, Shoot, Up}
import javafx.scene.control.Label
import utilities.MessageTypes.Warning

import scala.collection.immutable.HashSet
import scala.collection.mutable

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with GameScene {
  private val actions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false,
    Action(Movement, Some(Down)) -> false,
    Action(Movement, Some(Left)) -> false,
    Action(Movement, Some(Right)) -> false,
    Action(Shoot, None) -> false,
  )

  private val elements: Set[GameElements] = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())

  private var userLifeLabel: Label = _

    val scene: Scene = new Scene(dungeon) {
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

    userLifeLabel = new Label(PreferencesHandler.playerName + ": " + arena.get.player.lives + "Score: " + arena.get.player.score)
    userLifeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 40; -fx-text-fill: #92de34; -fx-font-weight: bold; -fx-background-color: #4444; " +
      "-fx-padding: 8px; -fx-background-radius: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);")
    userLifeLabel.relocate(Game.width / 3.5, 1)
    dungeon.getChildren.add(userLifeLabel)

    stage.setScene(scene)
    stage.show()

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
      Platform.runLater(() =>userLifeLabel.setText(
        PreferencesHandler.playerName + ": " + arena.get.player.lives + " \u2764 "
          + " Score: " + arena.get.player.score + " \u2605"))

    if (!arena.get.player.isLive) view.get.windowManager.showMessage("GAME OVER", "You lose", Warning)

  }

  def endLevel(): Unit = ???

  def endGame(): Unit = {

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
