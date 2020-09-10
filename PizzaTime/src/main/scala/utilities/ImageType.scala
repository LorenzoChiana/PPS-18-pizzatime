package utilities

import java.lang.System.getProperty


sealed trait ImageType
case object FloorImage extends ImageType { val path: String = "/images/textures/tile.png" }
case object WallImage extends ImageType { val path: String = "/images/textures/wallKitchen.png" }
case object Obstacle1Image extends ImageType { val path: String = "/images/sprites/obstacle1.png" }
case object Obstacle2Image extends ImageType { val path: String = "/images/sprites/obstacle2.png" }
case object Obstacle3Image extends ImageType { val path: String = "/images/sprites/obstacle3.png" }
case object BonusLifeImage extends ImageType { val path: String = "/images/sprites/pizza.png" }
case object HeroImage extends ImageType { val path: String = "/images/sprites/hero.png" }
case object EnemyImage extends ImageType { val path: String = "/images/sprites/enemy.png" }
case object BulletImage extends ImageType { val path: String = "/images/sprites/tomato.png" }
case object BonusScoreImage extends ImageType { val path: String = "/images/sprites/flour.png" }
