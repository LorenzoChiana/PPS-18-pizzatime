package utilities

import gamelogic.{LivingEntity, MovableEntity}
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
   * @param bi the [[BigInt]] to convert
   * @return the converted [[Int]]
   */
  implicit def bigIntToInt(bi: BigInt): Int = bi.intValue

  /** Implicit conversion from [[ Map[String, BigInt] ]] to [[ Map[String, Int] ]]
   *
   * @param m the map to convert
   * @return the converted map
   */
  implicit def bigIntToInt(m: Map[String, BigInt]): Map[String, Int] = m.transform((_, value) => value.intValue)

  /** Implicit conversion from [[DifficultyVal]] to [[String]]
   *
   * @param dv the [[DifficultyVal]] to convert
   * @return the converted [[String]]
   */
  implicit def diffValToString(dv: DifficultyVal): String = dv.toString

  /** Implicit conversion from [[Difficulty.Value]] to [[String]]
   *
   * @param dv the [[Difficulty.Value]] to convert
   * @return the converted [[String]]
   */
  implicit def diffValueToString(dv: Difficulty.Value): String = dv.toString

  implicit def movableEntityToLivingEntity(me: MovableEntity): LivingEntity = me.asInstanceOf[LivingEntity]
}