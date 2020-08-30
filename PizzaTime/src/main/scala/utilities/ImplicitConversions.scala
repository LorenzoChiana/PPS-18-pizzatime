package utilities

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
  implicit def bigIntToInt(bi: BigInt): Int = bi.intValue
  implicit def bigIntToInt(m: Map[String, BigInt]): Map[String, Int] = m.transform((_, value) => value.intValue)
  implicit def diffValToString(dv: DifficultyVal): String = dv.toString
  implicit def diffValueToString(dv: Difficulty.Value): String = dv.toString
}