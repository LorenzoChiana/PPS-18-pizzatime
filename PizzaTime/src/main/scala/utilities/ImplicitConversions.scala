package utilities

import scala.language.implicitConversions

object ImplicitConversions {
  implicit def tupleToPoint(t: (Int, Int)): Point = Point(t._1, t._2)
}