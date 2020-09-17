package gameview.fx.gamesceneelements


import gamelogic.EnemyCharacter
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.GameElements.{createElement, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.ImageView
import utilities.EnemyImage

import scala.collection.immutable

/**Set of [[ImageView]] representing [[Enemies]]*/
class Enemies extends GameElements{
  var enemies: Map[Int, ImageView] = arena.get.enemies.map(a => a.id -> createElement(images(EnemyImage))).toMap

  /**
   * Updating position enemy
   */
  def update(): Unit =
    enemies.foreach(e =>
      arena.get.enemies.find(_.id.equals(e._1)) match{
        case None =>  enemies = enemies - e._1
          Platform.runLater(() => dungeon.getChildren.remove(e._2))
        case Some(enemyAlive) =>  val pos = pointToPixel(enemyAlive.position.point)
          Platform.runLater(() => e._2 relocate(pos._1, pos._2))
      })
}

object Enemies{
  /** Creates a [[Enemies]].
   *
   *  @return the new [[Enemies]] instance
   */
  def apply(): Enemies = {
    val e: Enemies = new Enemies()
    Platform.runLater(() => e.enemies.foreach(enemy => dungeon.getChildren.add(enemy._2)))
    e
  }
}