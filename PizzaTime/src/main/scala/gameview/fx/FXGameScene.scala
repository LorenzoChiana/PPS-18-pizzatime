package gameview.fx

import gameview.{SpriteAnimation, Window}
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gameview.fx.FXGameScene.{createTile, tileHeight, tileWidth}
import gameview.scene.GameScene
import javafx.animation.AnimationTimer
import javafx.scene.{Group, Scene}
import javafx.scene.input.KeyCode.{DOWN, LEFT, RIGHT, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Down, Left, Right, Up}

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
  private var goNorth, goSouth, goEast, goWest: Boolean = false
  private val width: Int = stage.getWidth.intValue()
  private val height: Int = stage.getHeight.intValue()
  var animation: SpriteAnimation = _

  Platform.runLater(() => {
    floorImage = new Image("https://i.pinimg.com/originals/a4/22/9a/a4229a483cf76e0b5458450c2e591ff3.png")
    wallImage = new Image("https://i.pinimg.com/originals/cc/bc/92/ccbc92a6cdd9b42d856933c2fbf00677.jpg")

    val arenaArea: GridPane = createArena()
    val dungeon: Group = new Group(arenaArea, hero)
    animation = new SpriteAnimation(hero, Duration.millis(300), 4, 4, 0, 0, 100,130)

    val scene: Scene = new Scene(dungeon, width, height) {
      setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => goNorth = true
        case DOWN => goSouth = true
        case LEFT => goWest = true
        case RIGHT => goEast = true
        case _ => None
      })

      setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => goNorth = false
        case DOWN => goSouth = false
        case LEFT => goWest = false
        case RIGHT => goEast = false
        case _ => None
      })
    }
    stage.setScene(scene)
    stage.show()

    // AnimationTimer è un timer che viene chiamato ad ogni frame (per disattivarlo metodo stop)
    val timer: AnimationTimer = (_: Long) => {
      var dx, dy: Int = 0
       if (goNorth) {
         dy = dy - 1
         animation.play()
         animation.offsetY = 260
       }else if (goSouth) {
         dy = dy + 1
         animation.play()
         animation.offsetY = 0
       }else if (goEast) {
         dx = dx + 1
         animation.play()
         animation.offsetY = 390
       }else if (goWest){
         dx = dx - 1
         animation.play()
         animation.offsetY = 130
       } else {
         animation.stop()
       }
      moveBy(dx, dy)
    }
    timer.start()
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

    gridPane.setGridLinesVisible(false)

    gridPane
  }

  private def moveBy(dx: Int, dy: Int): Unit = {
    val cx: Double = hero.getBoundsInLocal.getWidth / 2
    val cy: Double = hero.getBoundsInLocal.getHeight / 2

    val x: Double = cx + hero.getLayoutX + dx
    val y: Double = cy + hero.getLayoutY + dy

    moveTo(x,y)
  }

  private def moveTo(x: Double, y: Double): Unit = {
    val cx: Double = hero.getBoundsInLocal.getWidth / 2
    val cy: Double = hero.getBoundsInLocal.getHeight / 2

    if (x - cx >= 0 &&
      x + cx <= width &&
      y - cy >= 0 &&
      y + cy <= height) {
      hero.relocate(x - cx, y - cy)
    }
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
