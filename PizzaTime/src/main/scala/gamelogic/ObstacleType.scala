package gamelogic

/** Types of [[Obstacle]]s. */
sealed trait ObstacleType {
  val allObstacleTypes: Seq[ObstacleType] = Seq(Table, Sink, Stove)
}

case object Table extends ObstacleType
case object Sink extends ObstacleType
case object Stove extends ObstacleType