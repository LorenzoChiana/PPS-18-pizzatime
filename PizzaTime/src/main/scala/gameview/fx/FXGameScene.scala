package gameview.fx

import gameview.Window
import gameview.scene.Scene
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gameview.fx.FXGameScene.{tileHeight, tileWidth}

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private var floorImage: Image = _
  private var wallImage: Image = _
  Platform.runLater(() => {
    floorImage = new Image("https://i.pinimg.com/originals/a4/22/9a/a4229a483cf76e0b5458450c2e591ff3.png")
    wallImage = new Image("https://i.pinimg.com/originals/cc/bc/92/ccbc92a6cdd9b42d856933c2fbf00677.jpg")

    val grid = initGrid(arenaWidth, arenaHeight)
    val gridPane = createArena(grid)

    val scene: javafx.scene.Scene = new javafx.scene.Scene(gridPane)

    stage.setScene(scene)
    stage.show()
  })

  private def initGrid(nRows: Int, nCols: Int):Array[Array[Image]] = {
    val walls = arena.get.walls
    val grid = Array.tabulate(nRows,nCols)( (_,_) => floorImage )

    walls.foreach(wall => {
      grid(wall.position.point.x)(wall.position.point.y) = wallImage
      /*
      val x: Int = pointToPixel(wall.position.point)._1.intValue()
      val y: Int = pointToPixel(wall.position.point)._2.intValue()
      grid(x)(y) = wallImage
       */
    })
    grid
  }


  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(grid: Array[Array[Image]]): GridPane = {
    val gridPane = new GridPane
    // questo è da canc mi serve solo per capire se ho generato bene i quadrati
    /*gridPane.setHgap(1)
    gridPane.setVgap(1)*/
    for (
      x <- 0 until arenaWidth ;//by tileWidth.toInt;
      y <- 0 until arenaHeight //by tileHeight.toInt
    ) {
      val imageView: ImageView = new ImageView(grid(x)(y))
      imageView.setFitWidth(tileWidth)
      imageView.setFitHeight(tileHeight)
      gridPane.add(imageView, x, y)
    }
    gridPane
  }
}

/** Factory for [[FXGameScene]] instances. */
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
}
