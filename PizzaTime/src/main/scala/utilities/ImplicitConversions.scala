package utilities

import scala.language.implicitConversions

/** Useful implicit conversions. */
object ImplicitConversions {
  /** Implicit conversion from [[Tuple2]] to [[Point]].
   *
   *  @param tuple the [[Tuple2]] to convert
   *  @return the converted [[Point]]
   */
  implicit def tupleToPoint(tuple: (Int, Int)): Point = Point(tuple._1, tuple._2)
}