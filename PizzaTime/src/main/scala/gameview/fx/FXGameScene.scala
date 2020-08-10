package gameview.fx

import gamemanager.handlers.PreferencesHandler
import gameview.Window
import gameview.scene.Scene
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with Scene {
  private var floorImage: Image = _
  private var wallImage: Image = _
  private val arenaWidth = PreferencesHandler.difficulty.arenaWidth
  private val arenaHeight = PreferencesHandler.difficulty.arenaHeight

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
    val arena = Array.tabulate(nRows,nCols)( (_,_) => floorImage )
    for (
      x <- 0 until nRows ;//by tileWidth.toInt;
      y <- 0 until nCols //by tileHeight.toInt
    ) {
      if ((x == 0) || ((x == nRows - 1) || (y == 0) || (y == nCols - 1))) {
        arena(x)(y) = wallImage
      }
    }
    arena
  }

  private def createArena(grid: Array[Array[Image]]): GridPane = {
    val gridPane = new GridPane
    gridPane.setHgap(1)
    gridPane.setVgap(1)
    for (
      x <- 0 until arenaWidth ;
      y <- 0 until arenaHeight
    ) {
      val imageView: ImageView = new ImageView(grid(x)(y))
      imageView.setFitWidth(Game.width / arenaWidth)
      imageView.setFitHeight(Game.height / arenaHeight)
      gridPane.add(imageView, x, y)
    }
    gridPane
  }
}
