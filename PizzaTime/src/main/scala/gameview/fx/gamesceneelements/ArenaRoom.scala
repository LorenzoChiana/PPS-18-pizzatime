package gameview.fx.gamesceneelements

import gamelogic.GameState.arena
import gamemanager.ImageLoader
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane

import scala.util.Random.between


/** [[GridPane]] representing [[ArenaRoom]]*/
class ArenaRoom extends GameElements {
  val arenaArea: GridPane = createArena()

  /**
   * Checks if the door should be opened
   */
  override def update(): Unit = {

  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): GridPane = {
    val gridPane = new GridPane

    for (f <- arena.get.floor) gridPane.add(createTile(ImageLoader.floorImage), f.position.point.x, f.position.point.y)
    for (w <- arena.get.walls) gridPane.add(createTile(ImageLoader.wallImage), w.position.point.x, w.position.point.y)
    for (o <- arena.get.obstacles){
      gridPane.add(createTile(ImageLoader.obstacles(between(0,3))), o.position.point.x, o.position.point.y)
    }
    gridPane.setGridLinesVisible(false)

    gridPane
  }
}
/** Factory for [[ArenaRoom]] instances. */
object ArenaRoom{
  /** Creates a [[ArenaRoom]].
   *
   *  @return the new [[ArenaRoom]] instance
   */
  def apply(): ArenaRoom = {
    val arena: ArenaRoom = new ArenaRoom()
    dungeon.getChildren.add(arena.arenaArea)
    arena
  }
}
