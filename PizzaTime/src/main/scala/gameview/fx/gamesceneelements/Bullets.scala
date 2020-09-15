package gameview.fx.gamesceneelements

import gamelogic.Bullet
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import utilities.BulletImage
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.GameElements.{addToDungeon, pointToPixel, setDimension, tileHeight, tileWidth}
import javafx.application.Platform
import javafx.scene.image.ImageView

import scala.collection.immutable

/**Set of [[ImageView]] representing [[Bullets]]*/
class Bullets extends GameElements{
  private var bullets: immutable.Map[Bullet, ImageView] = Map[Bullet, ImageView]()

  /**
   * Updating bullets
   */
  override def update(): Unit ={
    arena.get.bullets.foreach(b => addBullet(b))

    bullets.foreach(b => {
      arena.get.bullets.find(_ == b._1) match {
        case Some(unexplodeBullet) => val pos = pointToPixel(unexplodeBullet.position.point)
          Platform.runLater(() => b._2 relocate(pos._1, pos._2))
        case None => bullets = bullets -  b._1
          Platform.runLater(() => dungeon.getChildren.remove(b._2))
      }
    })
  }

  /**
   * Add bullet to the arena
   *  @param b bullet
   */
  private def addBullet(b: Bullet): Unit = {
    if (!bullets.contains(b))
      bullets = bullets + (b -> setDimension(addToDungeon(b, images(BulletImage)),tileHeight / 2, tileWidth / 2))
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