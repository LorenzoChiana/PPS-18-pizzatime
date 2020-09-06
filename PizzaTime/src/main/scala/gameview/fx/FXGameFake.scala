package gameview.fx

import gamelogic.GameState.{arena, arenaHeight, arenaWidth}
import gamemanager.handlers.PreferencesHandler
import gameview.Window
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.Scene
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.{Button, Label}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.KeyCode.{A, D, J, S, W}
import javafx.scene.input.KeyEvent
import javafx.scene.layout.{AnchorPane, GridPane}
import javafx.stage.Stage
import javafx.util.Duration
import utilities.WindowSize.Game
import utilities.{Action, Down, Left, Movement, Point, Right, Shoot, Up}

import scala.collection.immutable.HashSet
import scala.collection.mutable

case class FXGameFake(override val windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private val actions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false,
    Action(Movement, Some(Down)) -> false,
    Action(Movement, Some(Left)) -> false,
    Action(Movement, Some(Right)) -> false,
    Action(Shoot, None) -> false,
  )

  private var elements: Set[GameElements] = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())

  @FXML protected var root: GridPane = _
  @FXML protected var gameContainer: AnchorPane = _
  @FXML protected var statsPane: GridPane = _
  @FXML protected var lifeLabel: Label = _
  @FXML protected var levelLabel: Label = _
  @FXML protected var scoreLabel: Label = _
  @FXML protected var recordLabel: Label = _
  @FXML protected var backButton: Button = _

  val buttonStyle: String = "-fx-background-color: #f6f6f6; -fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; " +
    "-fx-font-weight: bold; -fx-padding: 8px; -fx-background-radius:1; -fx-border-color: #c3c3c3; -fx-border-width: 2 2 2 2;"
  val buttonStyleHover: String = "-fx-background-color: #c3c3c3; -fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; " +
    "-fx-font-weight: bold; -fx-padding: 8px; -fx-background-radius:1; -fx-border-color: #c3c3c3; -fx-border-width: 2 2 2 2;"

  backButton.setAlignment(Pos.CENTER)
  backButton.setStyle(buttonStyle)
  backButton.setOnMouseEntered(_ => backButton.setStyle(buttonStyleHover))
  backButton.setOnMouseExited(_ => backButton.setStyle(buttonStyle))
  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.notifyEndGame()))

  initLabels()

  statsPane.setMinWidth(150)
  statsPane.setAlignment(Pos.CENTER)

  statsPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #c9beeb, #a8c0ce); ")

  stage.getScene.setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
    case W => actions(Action(Movement, Some(Up))) = true
    case S => actions(Action(Movement, Some(Down))) = true
    case A => actions(Action(Movement, Some(Left))) = true
    case D => actions(Action(Movement, Some(Right))) = true
    case J => actions(Action(Shoot, None)) = true
    case _ => None
  })
  stage.getScene.setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
    case W => actions(Action(Movement, Some(Up))) = false
    case S => actions(Action(Movement, Some(Down))) = false
    case A => actions(Action(Movement, Some(Left))) = false
    case D => actions(Action(Movement, Some(Right))) = false
    case J => actions(Action(Shoot, None)) = false
    case _ => None
  })

  val timeline = new Timeline(new KeyFrame(Duration.millis(80), (_: ActionEvent) => {
    actions.foreach(d => if (d._2) FXWindow.observers.foreach(_.notifyAction(d._1)))
  }))
  timeline.setCycleCount(Animation.INDEFINITE)
  timeline.play()

  def initLabels(): Unit = {
    lifeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; -fx-font-weight: bold; -fx-padding: 8px;")
    lifeLabel.setText(PreferencesHandler.playerName + ": " + arena.get.player.lives)
    levelLabel.setStyle("-fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; -fx-font-weight: bold; -fx-padding: 8px;")
    levelLabel.setText("Level: " + arena.get.mapGen.currentLevel)
    scoreLabel.setStyle("-fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; -fx-font-weight: bold; -fx-padding: 8px;")
    scoreLabel.setText("Score: " + arena.get.player.score)
    recordLabel.setStyle("-fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; -fx-font-weight: bold; -fx-padding: 8px;")
    recordLabel.setText("Record: " + arena.get.player.record)
  }



  /**
   * method called by the controller cyclically to update the view
   */
  def updateView(): Unit = {
    elements.foreach(e => e.update())

    /** Updating player's label */
    Platform.runLater(() => {
      lifeLabel.setText(PreferencesHandler.playerName + ": " + arena.get.player.lives)
      levelLabel.setText("Level: " + arena.get.mapGen.currentLevel)
      scoreLabel.setText("Score: " + arena.get.player.score )
      recordLabel.setText("Record: " + arena.get.player.record)
    })

    //if (arena.get.player.isDead) showAlertMessage()
  }

  def endLevel(): Unit = {
    Platform.runLater(() => dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
  }
}

/** Utility methods for [[FXGameFake]]. */
object FXGameFake {
  @FXML protected var dungeon: Group = _
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
