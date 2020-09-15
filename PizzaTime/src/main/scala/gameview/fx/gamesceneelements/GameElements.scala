package gameview.fx.gamesceneelements

import gamelogic.Entity
import gamelogic.GameState.{arenaHeight, arenaWidth}
import gameview.fx.FXGameScene.dungeon
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import utilities.Point
import utilities.WindowSize.Game

/** Represents a basic elements of [[FXGameScene]].
 *  Implemented by [[ArenaRoom]], [[Bullets]], [[Collectibles]], [[Enemies]] and [[Player]].
 */
trait GameElements {
  /**
   * Update the elements of the game
   * */
  def update(): Unit
}

object GameElements {
  /**
   * Add an element to the dungeon
   *
   * @param e entity to add to the dungeon
   * @param image [[Image]] of the entity
   *
   * @return added [[ImageView]]
   * */
  def addToDungeon(e: Entity, image: Image): ImageView = {
    val element = createElement(image)
    element.relocate(pointToPixel(e.position.point)._1, pointToPixel(e.position.point)._2)
    Platform.runLater(() => dungeon.getChildren.add(element))
    element
  }

  /**
   * Resize an element in the dungeon
   *
   * @param element element to resize
   * @param height the height of the element
   * @param width the width of the element
   *
   * @return resized [[ImageView]]
   * */
  def setDimension(element: ImageView, height: Double, width: Double): ImageView = {
    element.setFitHeight(height)
    element.setFitWidth(width)
    element
  }

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
   * @return an [[ImageView]] that represents the sprite of the element
   */
  def createElement(image: Image): ImageView = {
    val element: ImageView = new ImageView(image)
    element.setFitWidth(tileWidth)
    element.setFitHeight(tileHeight)
    element
  }
}
