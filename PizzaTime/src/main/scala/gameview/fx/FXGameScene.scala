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
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.input.KeyCode.{A, DOWN, LEFT, RIGHT, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Action, Down, Left, Movement, Point, Right, Shoot, Up}
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.GridPane

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

  private val root: GridPane = new GridPane()

  root.add(dungeon, 0,0)

  val statsPane = new GridPane()
  var backButton: Button = new Button("End game")
  val lifeLabel: Label = createLabel(PreferencesHandler.playerName + ": " + arena.get.player.lives,0)
  val levelLabel: Label = createLabel("Level: " + arena.get.mapGen.currentLevel, 1)
  val scoreLabel: Label = createLabel("Score: " + arena.get.player.score, 2)
  val recordLabel: Label = createLabel(" Record: " + arena.get.player.record, 3)

  val buttonStyle: String = "-fx-background-color: #f6f6f6; -fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; " +
    "-fx-font-weight: bold; -fx-padding: 8px; -fx-background-radius:1; -fx-border-color: #c3c3c3; -fx-border-width: 2 2 2 2;"
  val buttonStyleHover: String = "-fx-background-color: #c3c3c3; -fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; " +
    "-fx-font-weight: bold; -fx-padding: 8px; -fx-background-radius:1; -fx-border-color: #c3c3c3; -fx-border-width: 2 2 2 2;"

  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.notifyEndGame()))
  statsPane.add(backButton, 0,4)

  backButton.setAlignment(Pos.CENTER)
  backButton.setStyle(buttonStyle)
  backButton.setOnMouseEntered(_ => backButton.setStyle(buttonStyleHover))
  backButton.setOnMouseExited(_ => backButton.setStyle(buttonStyle))

  root.add(statsPane, 1,0)
  statsPane.setMinWidth(150)
  statsPane.setAlignment(Pos.CENTER)

  statsPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #c9beeb, #a8c0ce); ")

  stage.getScene.setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
    case UP => actions(Action(Movement, Some(Up))) = true
    case DOWN => actions(Action(Movement, Some(Down))) = true
    case LEFT => actions(Action(Movement, Some(Left))) = true
    case RIGHT => actions(Action(Movement, Some(Right))) = true
    case A => actions(Action(Shoot, None)) = true
    case _ => None
  })
  stage.getScene.setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
    case UP => actions(Action(Movement, Some(Up))) = false
    case DOWN => actions(Action(Movement, Some(Down))) = false
    case LEFT => actions(Action(Movement, Some(Left))) = false
    case RIGHT => actions(Action(Movement, Some(Right))) = false
    case A => actions(Action(Shoot, None)) = false
    case _ => None
  })

  stage.getScene.setRoot(root)
  stage.setWidth(statsPane.getMinWidth + stage.getWidth)

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

    /** Updating player's label */
    Platform.runLater(() => {
      lifeLabel.setText(PreferencesHandler.playerName + ": " + arena.get.player.lives)
      levelLabel.setText("Level: " + arena.get.mapGen.currentLevel)
      scoreLabel.setText("Score: " + arena.get.player.score )
      recordLabel.setText("Record: " + arena.get.player.record)
    })

    if (arena.get.player.isDead) showAlertMessage()
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
    Platform.runLater(() => dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
    //Platform.runLater(() => dungeon.getChildren.add(userStatsLabel))
  }

  def createLabel(text: String, rowIndex: Int): Label = {
    val label: Label = new Label(text)
    label.setStyle("-fx-font-style: italic; -fx-font-size: 25; -fx-text-fill: #5c656c; -fx-font-weight: bold; -fx-padding: 8px;")
    statsPane.add(label, 0, rowIndex)
    label
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
