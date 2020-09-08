package gameview.fx.gamesceneelements

import gamelogic.Entity
import gamelogic.GameState.arena
import gamemanager.ImageLoader
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import scala.util.Random.between


/** [[GridPane]] representing [[ArenaRoom]]*/
class ArenaRoom extends GameElements {
  /**
   * Checks if the door should be opened
   */
  override def update(): Unit = createDoor()

  val door: ImageView = createTile(ImageLoader.floorImage)
  var positionDoor: (Double, Double) = _
  private def createDoor(): Unit = {
    Platform.runLater(() => {
      if(arena.get.door.isDefined && !dungeon.getChildren.contains(door)) {
        dungeon.getChildren.add(door)
        positionDoor = (pointToPixel(arena.get.door.get.position.point)._1, pointToPixel(arena.get.door.get.position.point)._2)
        door.relocate(positionDoor._1, positionDoor._2)
      } else if(arena.get.door.isEmpty){
        dungeon.getChildren.remove(door)
        for (w <- arena.get.walls) :+ (w, ImageLoader.wallImage)

      }
    })
  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): Unit = {
    for (f <- arena.get.floor) :+ (f, ImageLoader.floorImage)
    for (w <- arena.get.walls) :+ (w, ImageLoader.wallImage)
    for (o <- arena.get.obstacles) :+ (o, ImageLoader.obstacles(between(0, 3)))

    createDoor()
  }

  private def :+ (e: Entity, image: Image): Unit = {
    val tile = createTile(image)
    tile.relocate(pointToPixel(e.position.point)._1, pointToPixel(e.position.point)._2)
    Platform.runLater(() => dungeon.getChildren.add(tile))
  }
}

object ArenaRoom{
  /** Creates a [[ArenaRoom]].
   *
   *  @return the new [[ArenaRoom]] instance
   */
  def apply(): ArenaRoom = {
    val arena: ArenaRoom = new ArenaRoom()
    arena.createArena()
    arena
  }
}
