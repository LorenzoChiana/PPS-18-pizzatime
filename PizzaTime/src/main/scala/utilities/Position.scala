package utilities

case class Position(point: Point, dir: Option[Direction])
object Position{

  /** Returns the adjacent [[Point]] in a given [[Direction]].
   *
   *  @param dir the [[Direction]] to consider
   *  @return the new [[Position]]
   */
  def changePosition(pos: Position, dir: Direction): Position = dir match {
    case Up => Position(Point(pos.point.x, pos.point.y - 1), Some(dir))
    case Down => Position(Point(pos.point.x, pos.point.y + 1), Some(dir))
    case Left =>  Position(Point(pos.point.x - 1, pos.point.y), Some(dir))
    case Right => Position(Point(pos.point.x + 1, pos.point.y), Some(dir))
  }

  def changeDirection(pos: Position, newDir: Direction): Position = Position(pos.point, Some(newDir))
}