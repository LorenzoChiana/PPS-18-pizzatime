package gamemanager

import javafx.scene.image.Image
import utilities.{BonusLifeImage, BonusScoreImage, BulletImage, Enemy1Image, Enemy2Image, FloorImage, HeroImage, ImageType, LifeBarImage0, LifeBarImage1, LifeBarImage2, LifeBarImage3, LifeBarImage4, LifeBarImage5, SinkImage, StoveImage, TableImage, WallImage}

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Allows to load the various sprites of the game.
 *  The sprites are for:
 *    Floor
 *    Wall
 *    Obstacles
 *    Collectible
 *    Hero
 *    Enemies
 *    Bullets
 */
object ImageLoader {
  /**Map the imageType to its image*/
  var images: immutable.Map[ImageType, Image] = _

  /**
   * Allows to to load all images from files
   *
   * @return [[Future]]
   * */
  def loadImage(): Future[Unit] = Future {
   images = Map[ImageType, Image](FloorImage -> generateImage(FloorImage.path),
     WallImage -> generateImage(WallImage.path),
     TableImage -> generateImage(TableImage.path),
     StoveImage -> generateImage(StoveImage.path),
     SinkImage -> generateImage(SinkImage.path),
     BonusLifeImage -> generateImage(BonusLifeImage.path),
     BonusScoreImage -> generateImage(BonusScoreImage.path),
     HeroImage -> generateImage(HeroImage.path),
     Enemy1Image -> generateImage(Enemy1Image.path),
     Enemy2Image -> generateImage(Enemy2Image.path),
     BulletImage -> generateImage(BulletImage.path),
     LifeBarImage5 -> generateImage(LifeBarImage5.path),
     LifeBarImage4 -> generateImage(LifeBarImage4.path),
     LifeBarImage3 -> generateImage(LifeBarImage3.path),
     LifeBarImage2 -> generateImage(LifeBarImage2.path),
     LifeBarImage1 -> generateImage(LifeBarImage1.path),
     LifeBarImage0 -> generateImage(LifeBarImage0.path)
   )
  }

  private def generateImage(path: String): Image = new Image(getClass.getResourceAsStream(path))

}
