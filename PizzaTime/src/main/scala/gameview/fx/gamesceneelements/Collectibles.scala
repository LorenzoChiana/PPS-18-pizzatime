package gameview.fx.gamesceneelements

import gamelogic.{BonusLife, BonusScore, Collectible}
import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
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
      collectiblesTaken.foreach(c => collectibles(c).setVisible(false))
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
    arena.get.collectibles.foreach ( collectible => {
      var collectibleImage: ImageView = null
      collectible match {
        case _: BonusLife => collectibleImage = createTile(images(BonusLifeImage))
        case _: BonusScore => collectibleImage = createTile(images(BonusScoreImage))
      }
      coll.collectibles += (collectible -> collectibleImage)
      Platform.runLater(() => {
        dungeon.getChildren.add(collectibleImage)
        collectibleImage.relocate(pointToPixel(collectible.position.point)._1, pointToPixel(collectible.position.point)._2)
      })
    })
    coll
  }
}
