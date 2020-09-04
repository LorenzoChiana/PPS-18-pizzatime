package gameview.fx.gamesceneelements

import gamelogic.GameState.arena
import gamemanager.ImageLoader.heroImage
import gameview.SpriteAnimation
import gameview.fx.FXGameScene.{dungeon, pointToPixel, tileHeight, tileWidth}
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.util.Duration
import utilities.{Direction, Down, Left, Position, Right, Up}

/** [[ImageView]] representing [[Player]]*/
class Player extends GameElements{
  private val player: ImageView = new ImageView(heroImage)
  private var currentPosition: Position = arena.get.player.position

  val heroAnimation = new SpriteAnimation(player, Duration.millis(100), 4, 4, 0, 0, 100, 130)

  /**
   * Updating position and animate player
   */
  override def update(): Unit = {
    val playerPosition: Position = arena.get.player.position
    val playerDirection: Option[Direction] = arena.get.player.position.dir
    if (!playerPosition.equals(currentPosition)){
      playerDirection match {
        case Some(Up) => heroAnimation.offsetY = 260; heroAnimation.play()
        case Some(Down) => heroAnimation.offsetY = 0; heroAnimation.play()
        case Some(Left) => heroAnimation.offsetY = 130; heroAnimation.play()
        case Some(Right) => heroAnimation.offsetY = 390; heroAnimation.play()
        case _ => None
      }
      Platform.runLater(() => player.relocate(pointToPixel(playerPosition.point)._1, pointToPixel(playerPosition.point)._2))
      currentPosition = playerPosition
    }
  }

}
/** Factory for [[Player]] instances. */
object Player{

  /** Creates a [[Player]].
   *
   *  @return the new [[Player]] instance
   */
  def apply(): Player = {
    val p: Player = new Player()
    p.player setFitHeight tileHeight
    p.player setFitWidth tileWidth
    arena.get.player.position.dir match {
      case Some(Up) => p.heroAnimation.offsetY = 260; p.heroAnimation.play()
      case Some(Down) => p.heroAnimation.offsetY = 0; p.heroAnimation.play()
      case Some(Left) => p.heroAnimation.offsetY = 130; p.heroAnimation.play()
      case Some(Right) => p.heroAnimation.offsetY = 390; p.heroAnimation.play()
      case _ => None
    }
    Platform.runLater(() => {
      p.player relocate(pointToPixel(arena.get.player.position.point)._1, pointToPixel(arena.get.player.position.point)._2)
      dungeon.getChildren.add(p.player)
    })
    p
  }
}
