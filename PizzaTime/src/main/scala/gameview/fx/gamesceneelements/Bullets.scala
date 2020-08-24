package gameview.fx.gamesceneelements

import gamelogic.Bullet
import gamelogic.GameState.arena
import gamemanager.ImageLoader.bulletImage
import gameview.fx.FXGameScene
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel, tileHeight, tileWidth}
import javafx.application.Platform
import javafx.scene.image.ImageView
import scala.collection.immutable

/**Set of [[ImageView]] representing [[Bullets]]*/
class Bullets extends GameElements{
  private var bullets: immutable.Map[Bullet, ImageView] = new immutable.HashMap[Bullet, ImageView]()

  /**
   * Updating bullets
   */
  override def update(): Unit ={
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
  }

  /**
   * Add bullet to the arena
   *  @param b bullet
   */
  private def addBullet(b: Bullet): Unit = {
    if (!bullets.contains(b)) {
      val bullet = createTile(bulletImage)
      bullets = bullets + (b -> bullet)
      bullet.setFitHeight(tileHeight / 2)
      bullet.setFitWidth(tileWidth / 2)
      Platform.runLater(() => {
        FXGameScene.dungeon.getChildren.add(bullet)
      })
    }
  }
}

/** Factory for [[Bullets]] instances. */
object Bullets{
  /** Creates a [[Bullets]].
   *
   *  @return the new [[Bullets]] instance
   */
  def apply(): Bullets = {
    new Bullets()
  }
}