package utilities

import gamelogic.{Enemy, Hero, LivingEntity, MovableEntity}
import utilities.Difficulty.DifficultyVal

import scala.language.implicitConversions

/** Useful implicit conversions. */
object ImplicitConversions {

  /** Implicit conversion from [[Tuple2]] to [[Point]].
   *
   *  @param tuple the [[Tuple2]] to convert
   *  @return the converted [[Point]]
   */
  implicit def tupleToPoint(tuple: (Int, Int)): Point = Point(tuple._1, tuple._2)

  /** Implicit conversion from [[BigInt]] to [[Int]]
   *
   *  @param bi the [[BigInt]] to convert
   *  @return the converted [[Int]]
   */
  implicit def bigIntToInt(bi: BigInt): Int = bi.intValue

  /** Implicit conversion from [[ Map[String, BigInt] ]] to [[ Map[String, Int] ]]
   *
   *  @param m the map to convert
   *  @return the converted map
   */
  implicit def bigIntToInt(m: Map[String, BigInt]): Map[String, Int] = m.transform((_, value) => value.intValue)

  /** Implicit conversion from [[DifficultyVal]] to [[String]]
   *
   *  @param dv the [[DifficultyVal]] to convert
   *  @return the converted [[String]]
   */
  implicit def diffValToString(dv: DifficultyVal): String = dv.toString

  /** Implicit conversion from [[Difficulty.Value]] to [[String]]
   *
   *  @param dv the [[Difficulty.Value]] to convert
   *  @return the converted [[String]]
   */
  implicit def diffValueToString(dv: Difficulty.Value): String = dv.toString

  /** Implicit cast from [[MovableEntity]] to [[Hero]]
   *
   *  @param me the [[MovableEntity]] to cast
   *  @return the casted [[Hero]]
   */
  implicit def castMovableEntityToHero(me: MovableEntity): Hero = me match {
    case hero: Hero => hero
  }

  /** Implicit cast from [[LivingEntity]] to [[Hero]]
   *
   * @param l the [[LivingEntity]] to cast
   * @return the casted [[Hero]]
   */
  implicit def castLivingEntityToHero(l: LivingEntity): Hero = l match {
    case hero: Hero => hero
  }

  /** Implicit cast from [[LivingEntity]] to [[Enemy]]
   *
   * @param l the [[LivingEntity]] to cast
   * @return the casted [[Enemy]]
   */
  implicit def castLivingEntityToEnemy(l: LivingEntity): Enemy = l match {
    case enemy: Enemy => enemy
  }
}