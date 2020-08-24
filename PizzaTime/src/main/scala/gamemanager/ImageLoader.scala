package gamemanager

import javafx.scene.image.Image
import utilities.ImageType.{Floor, Wall, BonusLife, Obstacle, BonusScore, Hero, Enemy, Bullet}

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
  val floorImage: Image = generateImage(Floor.path)
  val wallImage: Image = generateImage(Wall.path)
  val obstacleImage : Image = generateImage(Obstacle.path)
  val bonusLifeImage: Image = generateImage(BonusLife.path)
  val heroImage: Image = generateImage(Hero.path)
  val enemyImage: Image = generateImage(Enemy.path)
  val bulletImage: Image = generateImage(Bullet.path)
  val bonusScoreImage: Image = generateImage(BonusScore.path)

  private def generateImage(path: String) = new Image(getClass.getResourceAsStream(path))
}
