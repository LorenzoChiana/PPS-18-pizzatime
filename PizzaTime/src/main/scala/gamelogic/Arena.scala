package gamelogic

/** The playable area, populated with all the [[Entity]]s. */
class Arena extends GameMap {
  val player: Player = ???
  var enemies: Set[EnemyCharacter] = Set()
  var bullets: Set[Bullet] = Set()
  var collectibles: Set[Collectible] = Set()
  var obstacles: Set[Obstacle] = Set()
  val walls: Set[Wall] = ???
  val floor: Set[Floor] = ???


  def allEntities: Set[Entity] = enemies ++ bullets ++ collectibles ++ obstacles ++ walls + player
}