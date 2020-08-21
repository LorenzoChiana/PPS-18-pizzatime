package gameview.fx

import gamelogic.{BonusLife, BonusScore, Bullet, Collectible, EnemyCharacter}
import gameview.SpriteAnimation
import gameview.Window
import javafx.application.Platform
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import utilities.WindowSize.Game
import gamelogic.GameState._
import gamemanager.ImageLoader
import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel, tileHeight, tileWidth}
import gameview.scene.GameScene
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.scene.{Group, Scene}
import javafx.scene.input.KeyCode.{DOWN, LEFT, RIGHT, SPACE, UP}
import javafx.scene.input.KeyEvent
import javafx.util.Duration
import utilities.{Action, Down, Left, Movement, Point, Right, Shoot, Up}
import javafx.scene.control.Label

import scala.collection.{immutable, mutable}

/**
 * Represents the scene that appears when you start playing
 * @param windowManager the window on which the scene is applied
 * @param stage a window in a JavaFX desktop application
 */
case class FXGameScene(windowManager: Window, stage: Stage) extends FXView(Some("GameScene.fxml")) with GameScene {

  private val hero: ImageView = new ImageView(ImageLoader.heroImage)
  private val directions: mutable.Map[Action, Boolean] = mutable.Map(Action(Movement, Some(Up)) -> false, Action(Movement, Some(Down)) -> false, Action(Movement, Some(Left)) -> false, Action(Movement, Some(Right)) -> false)
  private var enemies: immutable.Map[EnemyCharacter, ImageView] = new immutable.HashMap[EnemyCharacter, ImageView]()
  private var bullets: immutable.Map[Bullet, ImageView] = new immutable.HashMap[Bullet, ImageView]()
  private val width: Int = stage.getWidth.intValue()
  private val height: Int = stage.getHeight.intValue()
  var animation: SpriteAnimation = _
  private var collectibles: Map[Collectible, ImageView] = immutable.HashMap[Collectible, ImageView]()
  var currentPosition: Point = arena.get.player.position.point
  private var userLifeLabel: Label = _

  Platform.runLater(() => {
    hero.relocate(pointToPixel(currentPosition)._1, pointToPixel(currentPosition)._2)

    val arenaArea: GridPane = createArena()

    dungeon.getChildren.addAll(arenaArea,hero)
    animation = new SpriteAnimation(hero, Duration.millis(300), 4, 4, 0, 0, 100, 130)

    /** Create collectioble sprites */
    arena.get.collectibles.foreach ( collectible => {
      var collectibleImage: ImageView = null
      collectible match {
        case _: BonusLife => collectibleImage = createTile(ImageLoader.bonusLifeImage)
        case _: BonusScore => collectibleImage = createTile(ImageLoader.bonusScoreImage)
      }
      collectibles += (collectible -> collectibleImage)
      dungeon.getChildren.add(collectibleImage)
      collectibleImage.relocate(pointToPixel(collectible.position.point)._1, pointToPixel(collectible.position.point)._2)
    })

    arena.get.enemies.foreach(e => {
      val enemy = createTile(ImageLoader.enemyImage)
      enemies = enemies + (e -> enemy)
      dungeon.getChildren.add(enemy)
    })

    val scene: Scene = new Scene(dungeon, width, height) {
      setOnKeyPressed((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => directions(Action(Movement, Some(Up))) = true
        case DOWN => directions(Action(Movement, Some(Down))) = true
        case LEFT => directions(Action(Movement, Some(Left))) = true
        case RIGHT => directions(Action(Movement, Some(Right))) = true
        case SPACE => directions(Action(Shoot, None)) = true
        case _ => None
      })

      setOnKeyReleased((keyEvent: KeyEvent) => keyEvent.getCode match {
        case UP => directions(Action(Movement, Some(Up))) = false
        case DOWN => directions(Action(Movement, Some(Down))) = false
        case LEFT => directions(Action(Movement, Some(Left))) = false
        case RIGHT => directions(Action(Movement, Some(Right))) = false
        case SPACE => directions(Action(Shoot, None)) = false
        case _ => None
      })
    }

    userLifeLabel = new Label(PreferencesHandler.playerName + ": " + arena.get.player.lives + "Score: " + arena.get.player.score)
    userLifeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 40; -fx-text-fill: #92de34; -fx-font-weight: bold; -fx-background-color: #4444; " +
      "-fx-padding: 8px; -fx-background-radius: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);")
    userLifeLabel.relocate(Game.width / 3.5, 1)
    dungeon.getChildren.add(userLifeLabel)

    stage.setScene(scene)
    stage.show()

    val timeline = new Timeline(new KeyFrame(Duration.millis(100), (_: ActionEvent) => {
      directions.foreach(d => if (d._2) FXWindow.observers.foreach(o => o.notifyAction(d._1)))
    }))
    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.play()

  })

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

      /** Updating collectibles */
      val collectiblesTaken = collectibles.keySet.diff(arena.get.collectibles)
      collectiblesTaken.foreach(collectible => collectibles(collectible).setVisible(false))
      collectibles --= collectiblesTaken
    }

    /** Updating position enemy */
    enemies.foreach(e => {
      val ens = arena.get.enemies.filter(_ == e._1)
      if (ens.isEmpty){
        //e._2.setVisible(false)
        Platform.runLater(() => {
          dungeon.getChildren.remove(e._2)
        })
      }else{
        val pos = pointToPixel(ens.head.position.point)
        e._2 relocate(pos._1, pos._2)
      }
    })

    /** Updating bullet */
    arena.get.bullets.foreach(b => addBullet(b))
    bullets.foreach(b => {
      val unexplodedBullet = arena.get.bullets.find(_ == b._1)
      if (unexplodedBullet.isEmpty)
        Platform.runLater(() => {
          dungeon.getChildren.remove(b._2)
        })
      else{
        val pos = pointToPixel(unexplodedBullet.get.position.point)
        b._2 relocate(pos._1, pos._2)
      }
    })

    /** Updating player lives */
    Platform.runLater(() =>userLifeLabel.setText(PreferencesHandler.playerName + ": " + arena.get.player.lives + " \u2764 " + " Score: " + arena.get.player.score + " \u2605"))
  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): GridPane = {
    val gridPane = new GridPane

    for (floor <- arena.get.floor) gridPane.add(createTile(ImageLoader.floorImage), floor.position.point.x, floor.position.point.y)
    for (wall <- arena.get.walls) gridPane.add(createTile(ImageLoader.wallImage), wall.position.point.x, wall.position.point.y)
    for (obstacle <- arena.get.obstacles) gridPane.add(createTile(ImageLoader.obstacleImage), obstacle.position.point.x, obstacle.position.point.y)

    gridPane.setGridLinesVisible(false)

    gridPane
  }

  /**
   * Add bullet to the arena
   *
   */
  private def addBullet(b: Bullet): Unit = {
    if (!bullets.contains(b)) {
      val bullet = createTile(ImageLoader.bulletImage)
      bullets = bullets + (b -> bullet)
      bullet.setFitHeight(tileHeight / 2)
      bullet.setFitWidth(tileWidth / 2)
      Platform.runLater(() => {
        FXGameScene.dungeon.getChildren.add(bullet)
      })
    }
  }
}

/** Utility methods for [[FXGameScene]]. */
object FXGameScene {
  val dungeon: Group = new Group()
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
   * @return an [[ImageView]] that represents the sprite of the tile
   */
  def createTile(image: Image): ImageView = {
    val tile: ImageView = new ImageView(image)
    tile.setFitWidth(tileWidth)
    tile.setFitHeight(tileHeight)
    tile
  }
}
