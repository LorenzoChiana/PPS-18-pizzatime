package utilities

sealed trait ImageType
case object FloorImage extends ImageType { val path: String = "/images/textures/tile.png" }
case object WallImage extends ImageType { val path: String = "/images/textures/wallKitchen.png" }
case object HeroImage extends ImageType { val path: String = "/images/sprites/hero.png" }
case object EnemyImage extends ImageType { val path: String = "/images/sprites/enemy.png" }
case object BulletImage extends ImageType { val path: String = "/images/sprites/tomato.png" }
case object BonusLifeImage extends ImageType { val path: String = "/images/sprites/pizza.png" }
case object BonusScoreImage extends ImageType { val path: String = "/images/sprites/flour.png" }
case object LifeBarImage5 extends ImageType { val path: String = "/images/sprites/life5.png" }
case object LifeBarImage4 extends ImageType { val path: String = "/images/sprites/life4.png" }
case object LifeBarImage3 extends ImageType { val path: String = "/images/sprites/life3.png" }
case object LifeBarImage2 extends ImageType { val path: String = "/images/sprites/life2.png" }
case object LifeBarImage1 extends ImageType { val path: String = "/images/sprites/life1.png" }
case object LifeBarImage0 extends ImageType { val path: String = "/images/sprites/lifeBar/life0.png" }

sealed trait ObstacleImage extends ImageType { val allObstacleImages: Seq[ObstacleImage] = Seq(Obstacle1Image, Obstacle2Image, Obstacle3Image) }
case object Obstacle1Image extends ObstacleImage { val path: String = "/images/sprites/obstacle1.png" }
case object Obstacle2Image extends ObstacleImage { val path: String = "/images/sprites/obstacle2.png" }
case object Obstacle3Image extends ObstacleImage { val path: String = "/images/sprites/obstacle3.png" }
