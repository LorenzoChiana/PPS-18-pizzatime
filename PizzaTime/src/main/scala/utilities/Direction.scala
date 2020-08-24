package utilities

sealed trait Direction
case object Up extends Direction
case object Down extends Direction
case object Left extends Direction
case object Right extends Direction

case object ToUp {
  def apply(point: Point): Point = Point(point.x, point.y -1)
}

case object ToDown {
  def apply(point: Point): Point = Point(point.x, point.y +1)
}

case object ToLeft {
  def apply(point: Point): Point = Point(point.x -1, point.y)
}

case object ToRight {
  def apply(point: Point): Point = Point(point.x +1, point.y)
}