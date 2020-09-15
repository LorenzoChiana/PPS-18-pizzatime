package gameview.fx.gamesceneelements

import gamelogic.Entity
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}

/** Represents a basic elements of [[FXGameScene]].
 *  Implemented by [[ArenaRoom]], [[Bullets]], [[Collectibles]], [[Enemies]] and [[Player]].
 */
trait GameElements {
  def update(): Unit

  def addToDungeon(e: Entity, image: Image): ImageView = {
    val tile = createTile(image)
    tile.relocate(pointToPixel(e.position.point)._1, pointToPixel(e.position.point)._2)
    Platform.runLater(() => dungeon.getChildren.add(tile))
    tile
  }

  def setDimension(tile: ImageView, height: Double, width: Double): ImageView = {
    tile.setFitHeight(height)
    tile.setFitWidth(width)
    tile
  }
}
