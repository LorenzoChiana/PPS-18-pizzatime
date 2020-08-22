package gamemanager

import gamemanager.ImageLoader.floorImage
import javafx.scene.image.Image
import utilities.ImageType

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


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
  var floorImage: Image = _
  var wallImage: Image = _
  var obstacleImage: Image = _
  var bonusLifeImage: Image = _
  var heroImage: Image = _
  var enemyImage: Image = _
  var bulletImage: Image = _
  var bonusScoreImage: Image = _

  def generateImages(): Unit = {
    ImageType.allImage.foreach(i => i match{
      case ImageType.Floor => floorImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.Wall => wallImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.Obstacle => obstacleImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.BonusLife => bonusLifeImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.Hero => heroImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.Enemy => enemyImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.Bullet => bulletImage = new Image(getClass.getResourceAsStream(i.path))
      case ImageType.BonusScore => bonusScoreImage = new Image(getClass.getResourceAsStream(i.path))
    } )
  }

}
