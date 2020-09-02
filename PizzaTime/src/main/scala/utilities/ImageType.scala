package utilities

object ImageType {
  val Floor: ImageVal = ImageVal("/images/textures/tile.png")
  val Wall: ImageVal = ImageVal("/images/textures/wallKitchen.png")
  val Obstacle1: ImageVal = ImageVal("/images/sprites/obstacle1.png")
  val Obstacle2: ImageVal = ImageVal("/images/sprites/obstacle2.png")
  val Obstacle3: ImageVal = ImageVal("/images/sprites/obstacle3.png")
  val BonusLife: ImageVal = ImageVal( "/images/sprites/pizza.png")
  val Hero: ImageVal = ImageVal("/images/sprites/hero.png")
  val Enemy: ImageVal = ImageVal("/images/sprites/enemy.png")
  val Bullet: ImageVal = ImageVal("/images/sprites/tomato.png")
  val BonusScore: ImageVal = ImageVal("/images/sprites/flour.png")

  val allImage = Seq(Floor, Wall, Obstacle1, Obstacle2, Obstacle3 , BonusLife, Hero, Enemy, Bullet, BonusScore)
}

case class ImageVal(path: String)