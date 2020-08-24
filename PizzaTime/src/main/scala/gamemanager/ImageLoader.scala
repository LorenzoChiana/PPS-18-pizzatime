package gamemanager

import javafx.scene.image.Image
import utilities.ImageType.{BonusLife, BonusScore, Bullet, Enemy, Floor, Hero, Obstacle1, Obstacle2, Obstacle3, Wall}

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
  val obstacles = Seq(generateImage(Obstacle1.path),
                      generateImage(Obstacle2.path),
                      generateImage(Obstacle3.path))
  val bonusLifeImage: Image = generateImage(BonusLife.path)
  val heroImage: Image = generateImage(Hero.path)
  val enemyImage: Image = generateImage(Enemy.path)
  val bulletImage: Image = generateImage(Bullet.path)
  val bonusScoreImage: Image = generateImage(BonusScore.path)

  private def generateImage(path: String) = new Image(getClass.getResourceAsStream(path))
}
