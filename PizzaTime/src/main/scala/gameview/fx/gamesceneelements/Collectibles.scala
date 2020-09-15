package gameview.fx.gamesceneelements

import gamelogic.{BonusLife, BonusScore, Collectible}
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.GameElements.addToDungeon
import javafx.application.Platform
import javafx.scene.image.ImageView
import utilities.{BonusLifeImage, BonusScoreImage}

import scala.collection.immutable

/**Set of [[ImageView]] representing [[Collectibles]]*/
class Collectibles extends GameElements{
  private var collectibles: immutable.Map[Collectible, ImageView] = new immutable.HashMap[Collectible, ImageView]()

  /**
   * Updating collectibles
   */
  def update(): Unit ={
    Platform.runLater(() => {
      val collectiblesTaken = collectibles.keySet.diff(arena.get.collectibles)
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
      case collectible@(_: BonusLife) => coll.collectibles += (collectible -> addToDungeon(collectible, images(BonusLifeImage)))
      case collectible@(_: BonusScore) => coll.collectibles += (collectible -> addToDungeon(collectible, images(BonusScoreImage)))
    }
    coll
  }
}
