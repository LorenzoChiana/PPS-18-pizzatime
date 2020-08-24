package gameview.fx.gamesceneelements


import gamelogic.EnemyCharacter
import gamelogic.GameState.arena
import gamemanager.ImageLoader.enemyImage
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.scene.image.ImageView

import scala.collection.immutable

/**Set of [[ImageView]] representing [[Enemies]]*/
class Enemies extends GameElements{
  var enemies: immutable.Map[EnemyCharacter, ImageView] = arena.get.enemies.map(a => a -> createTile(enemyImage)).toMap

  /**
   * Updating position enemy
   */
  def update(): Unit ={
    enemies.foreach(e => {
      val enemyAlive = arena.get.enemies.find(_ == e._1)
      if (enemyAlive.isEmpty) {e._2.setVisible(false); enemies = enemies - e._1}
      else{
        val pos = pointToPixel(enemyAlive.get.position.point)
        e._2 relocate(pos._1, pos._2)
      }
    })
  }
}

/** Factory for [[Enemies]] instances. */
object Enemies{
  /** Creates a [[Enemies]].
   *
   *  @return the new [[Enemies]] instance
   */
  def apply(): Enemies = {
    val e: Enemies = new Enemies()
    e.enemies.foreach(enemy => dungeon.getChildren.add(enemy._2))
    e
  }
}