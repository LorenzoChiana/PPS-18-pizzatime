package utilities

import javafx.scene.image.Image

object ImageType extends Enumeration {
  val Floor: ImageVal = ImageVal("/images/textures/garden.png")
  val Wall: ImageVal = ImageVal("/images/textures/wall.jpg")
  val Obstacle: ImageVal = ImageVal("/images/sprites/flour.png")
  val BonusLife: ImageVal = ImageVal( "/images/sprites/pizza.png")
  val Hero: ImageVal = ImageVal("/images/sprites/hero.png")
  val Enemy: ImageVal = ImageVal("/images/sprites/enemy.png")
  val Bullet: ImageVal = ImageVal("/images/sprites/tomato.png")
  val BonusScore: ImageVal = ImageVal("/images/sprites/tomato.png")

  val allImage = Seq(Floor, Wall, Obstacle, BonusLife, Hero, Enemy, Bullet, BonusScore)
}

case class ImageVal(path: String)