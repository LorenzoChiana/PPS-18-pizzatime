package gameview.fx

import gamelogic.GameState.{arena, arenaHeight, arenaWidth}
import gamemanager.GameManager.onBack
import gamemanager.handlers.PreferencesHandler
import gameview.Window
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.{ArenaRoom, Bullets, Collectibles, Enemies, GameElements, Player}
import gameview.scene.Scene
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.control.{Button, Label}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.KeyCode.{A, D, J, S, W}
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.util.Duration
import utilities.WindowSize.Game
import utilities.{Action, Down, Left, Movement, Point, Right, Shoot, Up, WindowSize}

import scala.collection.immutable.HashSet
import scala.collection.mutable

case class FXGameScene(override val windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private val actions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false,
    Action(Movement, Some(Down)) -> false,
    Action(Movement, Some(Left)) -> false,
    Action(Movement, Some(Right)) -> false,
    Action(Shoot, None) -> false,
  )

  private var elements: Set[GameElements] = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())

  @FXML protected var root: GridPane = _
  @FXML protected var statsPane: GridPane = _
  @FXML protected var lifeLabel: Label = _
  @FXML protected var levelLabel: Label = _
  @FXML protected var scoreLabel: Label = _
  @FXML protected var recordLabel: Label = _
  @FXML protected var endButton: Button = _
  @FXML protected var backButton: Button = _
  @FXML protected var loseLabel: Label = _

  root.add(dungeon, 0, 0)

  endButton.setOnMouseClicked(_ => FXWindow.observers.foreach(observer => {
    observer.notifyEndGame()
    showEndGameGraphic()
  }))
  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.onBack()))

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

  stage.setWidth(WindowSize.Game.width + statsPane.getMinWidth)

  val timeline = new Timeline(new KeyFrame(Duration.millis(80), (_: ActionEvent) => {
    actions.foreach(d => if (d._2) FXWindow.observers.foreach(_.notifyAction(d._1)))
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

    if (arena.get.player.isDead) showEndGameGraphic()
  }

  def endLevel(): Unit = {
    Platform.runLater(() => dungeon.getChildren.clear())
    elements = HashSet(ArenaRoom(), Player(), Enemies(), Collectibles(), Bullets())
  }

  private def showEndGameGraphic(): Unit = {
    loseLabel.setVisible(true)
    endButton.setDisable(true)
    backButton.setVisible(true)
  }
}

/** Utility methods for [[FXGameScene]]. */
object FXGameScene {
  @FXML val dungeon: Group = new Group()
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
