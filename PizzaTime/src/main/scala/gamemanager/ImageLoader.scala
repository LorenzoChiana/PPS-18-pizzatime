package gamemanager

import javafx.scene.image.Image


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

  private def generateImage(path: String) = new Image(getClass.getResourceAsStream(path))

   def generateImages():Unit = {
    floorImage = generateImage("/images/textures/garden.png")
    wallImage= generateImage("/images/textures/wall.jpg")
    obstacleImage = generateImage("/images/sprites/flour.png")
    bonusLifeImage = generateImage("/images/sprites/pizza.png")
    heroImage = generateImage("/images/sprites/hero.png")
    enemyImage = generateImage("/images/sprites/enemy.png")
    bulletImage = generateImage("/images/sprites/tomato.png")
    bonusScoreImage= generateImage("/images/sprites/tomato.png")
  }
}
