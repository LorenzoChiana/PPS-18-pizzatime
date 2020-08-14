package gameview.fx

import gameview.{SpriteAnimation, Window}
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gameview.fx.FXGameScene.{createTile, pointToPixel, tileHeight, tileWidth}
import gameview.scene.GameScene
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.util.Duration
import javafx.event.ActionEvent
import javafx.scene.{Group, Scene}
import javafx.scene.input.KeyCode.{DOWN, LEFT, RIGHT, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Action, Direction, Down, Left, Movement, Point, Right, Shoot, Up}

import scala.collection.mutable.Map
import utilities.Point

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with GameScene {
  private var floorImage: Image = _
  private var wallImage: Image = _

  private val heroImage: Image = new Image(getClass.getResourceAsStream("/images/sprite/sprite.png"))
  private val hero: ImageView = new ImageView(heroImage)
  private val directions: Map[Action, Boolean] = Map(Action(Movement, Some(Up)) -> false, Action(Movement, Some(Down)) -> false, Action(Movement, Some(Left)) -> false, Action(Movement, Some(Right)) -> false)
  private val width: Int = stage.getWidth.intValue()
  private val height: Int = stage.getHeight.intValue()
  var animation: SpriteAnimation = _


  Platform.runLater(() => {
    floorImage = new Image("https://i.pinimg.com/originals/a4/22/9a/a4229a483cf76e0b5458450c2e591ff3.png")
    wallImage = new Image("https://i.pinimg.com/originals/cc/bc/92/ccbc92a6cdd9b42d856933c2fbf00677.jpg")

    val arenaArea: GridPane = createArena()
    val dungeon: Group = new Group(arenaArea, hero)
    animation = new SpriteAnimation(hero, Duration.millis(300), 4, 4, 0, 0, 100, 130)

    val scene: Scene = new Scene(dungeon, width, height) {
      setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => directions(Action(Movement, Some(Up))) = true
        case DOWN => directions(Action(Movement, Some(Down))) = true
        case LEFT => directions(Action(Movement, Some(Left))) = true
        case RIGHT => directions(Action(Movement, Some(Right))) = true
        case _ => None
      })

      setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => directions(Action(Movement, Some(Up))) = false
        case DOWN => directions(Action(Movement, Some(Down))) = false
        case LEFT => directions(Action(Movement, Some(Left))) = false
        case RIGHT => directions(Action(Movement, Some(Right))) = false
        case _ => None
      })

    }
    stage.setScene(scene)
    stage.show()

    val timeline = new Timeline(new KeyFrame(Duration.millis(150), (_: ActionEvent) => {
      directions.foreach(d => if (d._2) FXWindow.observers.foreach(o => o.notifyAction(d._1)))
    }))
    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.play()
  })

  var currentPosition: Point = arena.get.player.position.point

  /**
   * method called by the controller cyclically to update the view
   */
  def updateView(): Unit = {
    /** Updating position and animate hero */
    if (!arena.get.player.position.point.equals(currentPosition)) {
      arena.get.player.position.dir match {
        case Some(Up) => animation.offsetY = 260; animation.play()
        case Some(Down) => animation.offsetY = 0; animation.play()
        case Some(Left) => animation.offsetY = 130; animation.play()
        case Some(Right) => animation.offsetY = 390; animation.play()
        case _ => None
      }
      hero.relocate(pointToPixel(arena.get.player.position.point)._1, pointToPixel(arena.get.player.position.point)._2)
      currentPosition = arena.get.player.position.point
    }
  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): GridPane = {
    val gridPane = new GridPane

    for (floor <- arena.get.floor) gridPane.add(createTile(floorImage), floor.position.point.x, floor.position.point.y)
    for (wall <- arena.get.walls) gridPane.add(createTile(wallImage), wall.position.point.x, wall.position.point.y)

    gridPane.setGridLinesVisible(false)

    gridPane
  }
}

/** Utility methods for [[FXGameScene]]. */
object FXGameScene {
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
  def pointToPixel(p: Point): (Double, Double) = (p.x * tileWidth, (p.y * tileHeight) + tileHeight)

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
