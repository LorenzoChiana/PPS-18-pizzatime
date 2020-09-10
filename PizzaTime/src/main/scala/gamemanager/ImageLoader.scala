package gamemanager

import javafx.scene.image.Image
import utilities.{BonusLifeImage, BonusScoreImage, BulletImage, EnemyImage, FloorImage, HeroImage, ImageType, Obstacle1Image, Obstacle2Image, Obstacle3Image, WallImage}

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
  var images: immutable.Map[ImageType, Image] = _

  def loadImage(): Future[Unit] = Future {
   images = Map[ImageType, Image]((FloorImage, generateImage(FloorImage.path)),
      (WallImage, generateImage(WallImage.path)),
      (Obstacle1Image, generateImage(Obstacle1Image.path)),
      (Obstacle2Image, generateImage(Obstacle2Image.path)),
      (Obstacle3Image , generateImage(Obstacle3Image.path)),
      (BonusLifeImage , generateImage(BonusLifeImage.path)),
      (BonusScoreImage , generateImage(BonusScoreImage.path)),
      (HeroImage , generateImage(HeroImage.path)),
      (EnemyImage , generateImage(EnemyImage.path)),
      (BulletImage , generateImage(BulletImage.path))
    )
  }

  private def generateImage(path: String): Image = new Image(getClass.getResourceAsStream(path))


}
