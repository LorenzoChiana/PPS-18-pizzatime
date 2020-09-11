package gameview.fx.gamesceneelements

import gamelogic.Entity
import gamelogic.GameState.arena
import gamemanager.ImageLoader
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import utilities.{FloorImage, Obstacle1Image, Obstacle2Image, Obstacle3Image, WallImage}

import scala.util.Random.between


/** [[GridPane]] representing [[ArenaRoom]]*/
class ArenaRoom extends GameElements {
  /**
   * Checks if the door should be opened
   */
  override def update(): Unit = createDoor()

  val door: ImageView = createTile(images(FloorImage))
  var positionDoor: (Double, Double) = _

  private def createDoor(): Unit = {
    Platform.runLater(() => {
      if(arena.get.door.isDefined && !dungeon.getChildren.contains(door)) {
        dungeon.getChildren.add(door)
        positionDoor = (pointToPixel(arena.get.door.get.position.point)._1, pointToPixel(arena.get.door.get.position.point)._2)
        door.relocate(positionDoor._1, positionDoor._2)
      } else if(arena.get.door.isEmpty && dungeon.getChildren.contains(door)){
        dungeon.getChildren.remove(door)
        val wall: ImageView = createTile(images(WallImage))
        wall.relocate(positionDoor._1, positionDoor._2)
        dungeon.getChildren.add(wall)
      }
    })
  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): Unit = {
    for (f <- arena.get.floor) :+ (f, images(FloorImage))
    for (w <- arena.get.walls) :+ (w, images(WallImage))

    for (o <- arena.get.obstacles) :+ (o,
      between(1, 4) match {
      case 1 => images(Obstacle1Image)
      case 2 => images(Obstacle2Image)
      case 3 => images(Obstacle3Image)
    })

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
