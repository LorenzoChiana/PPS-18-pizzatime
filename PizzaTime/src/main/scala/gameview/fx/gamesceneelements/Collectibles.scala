package gameview.fx.gamesceneelements

import gamelogic.{BonusLife, BonusScore}
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.GameElements.addToDungeon
import javafx.application.Platform
import javafx.scene.image.ImageView
import utilities.{BonusLifeImage, BonusScoreImage}

import scala.collection.immutable

/** Set of [[ImageView]] representing [[Collectibles]]. */
class Collectibles extends GameElements{
  private var collectibles: immutable.Map[Int, ImageView] = new immutable.HashMap[Int, ImageView]()

  /**
   * Updating collectibles
   */
  def update(): Unit = {
    Platform.runLater(() => {
      val collectiblesTaken = collectibles.keySet.diff(arena.get.collectibles.map(_.id))
      collectiblesTaken.foreach(c => dungeon.getChildren.remove(collectibles(c)))
      collectibles = collectibles -- collectiblesTaken
    })
  }
}

object Collectibles{

  /** Creates a [[Collectibles]].
   *
   *  @return the new [[Collectibles]] instance
   */
  def apply(): Collectibles = {
    val coll: Collectibles = new Collectibles()
    arena.get.collectibles.foreach {
      case c: BonusLife => coll.collectibles += (c.id -> addToDungeon(c, images(BonusLifeImage)))
      case c: BonusScore => coll.collectibles += (c.id -> addToDungeon(c, images(BonusScoreImage)))
    }
    coll
  }
}
