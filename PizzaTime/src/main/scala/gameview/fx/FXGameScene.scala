package gameview.fx

import gamelogic.{BonusLife, BonusScore, Collectible}
import gameview.Window
import gameview.scene.Scene
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gameview.fx.FXGameScene.{createTile, pointToPixel}
import utilities.Point

import scala.collection.immutable

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private var floorImage: Image = _
  private var wallImage: Image = _
  private var obstacleImage: Image = _
  private var collectibles: immutable.Map[Collectible, ImageView] = new immutable.HashMap[Collectible, ImageView]()

  Platform.runLater(() => {
    floorImage = new Image(getClass.getResourceAsStream("/images/textures/garden.png"))
    wallImage = new Image(getClass.getResourceAsStream("/images/textures/wall.jpg"))
    obstacleImage = new Image(getClass.getResourceAsStream("/images/sprites/flour.png"))

    val arenaArea: GridPane = createArena()
    val scene: javafx.scene.Scene = new javafx.scene.Scene(arenaArea)

    stage.setScene(scene)
    stage.show()
  })

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): GridPane = {
    val gridPane = new GridPane

    for (floor <- arena.get.floor) gridPane.add(createTile(floorImage), floor.position.point.x, floor.position.point.y)
    for (wall <- arena.get.walls) gridPane.add(createTile(wallImage), wall.position.point.x, wall.position.point.y)
    for (obstacle <- arena.get.obstacles) gridPane.add(createTile(obstacleImage), obstacle.position.point.x, obstacle.position.point.y)

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
