package gameview.fx.gamesceneelements

import gamelogic.Bullet
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import utilities.BulletImage
import gameview.fx.FXGameScene
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel, tileHeight, tileWidth}
import javafx.application.Platform
import javafx.scene.image.ImageView

import scala.collection.{immutable, mutable}

/**Set of [[ImageView]] representing [[Bullets]]*/
class Bullets extends GameElements{
  private var bullets: immutable.Map[Bullet, ImageView] = Map[Bullet, ImageView]()

  /**
   * Updating bullets
   */
  override def update(): Unit ={
    println("bullet " + bullets.size)
    arena.get.bullets.foreach(b => addBullet(b))

    bullets.foreach(b => {
      val unexplodedBullet = arena.get.bullets.find(_ == b._1)

      if (unexplodedBullet.isEmpty) {
        bullets = bullets -  b._1
        Platform.runLater(() => dungeon.getChildren.remove(b._2))
      } else{
        val pos = pointToPixel(unexplodedBullet.get.position.point)
        Platform.runLater(() => b._2 relocate(pos._1, pos._2))
      }
    })
  }

  /**
   * Add bullet to the arena
   *  @param b bullet
   */
  private def addBullet(b: Bullet): Unit = {
    if (!bullets.contains(b)) {
      val bullet = createTile(images(BulletImage))
      bullets = bullets + (b -> bullet)
      bullet.setFitHeight(tileHeight / 2)
      bullet.setFitWidth(tileWidth / 2)

      Platform.runLater(() => {
        val pos = pointToPixel(b.position.point)
        bullet.relocate(pos._1, pos._2)
        FXGameScene.dungeon.getChildren.add(bullet)
      })
    }
  }
}

object Bullets{
  /** Creates a [[Bullets]].
   *
   *  @return the new [[Bullets]] instance
   */
  def apply(): Bullets = {
    new Bullets()
  }
}