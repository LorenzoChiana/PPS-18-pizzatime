package utilities

import javafx.animation.FadeTransition
import javafx.scene.Node
import javafx.util.Duration
import javafx.application.Platform.runLater

/** Provides animations between scenes. */
object Animations {
  val FADE_DURATION: Int = 400
  val OPACITY_FADE_IN: Int = 0
  val OPACITY_FADE_OUT: Int = 1

  object Fade {
    /** The fade in for the scene
     *
     *  @param scene the new scene
     */
    def fadeIn(scene: Node): Unit = {
      require(scene != null)
      scene.setOpacity(OPACITY_FADE_IN)
      fade(scene, 0, 1)
    }

    /** The fade out for the scene
     *
     *  @param scene the old scene
     */
    def fadeOut(scene: Node): Unit = {
      require(scene != null)
      scene.setOpacity(OPACITY_FADE_OUT)
      fade(scene, 1, 0)
    }

    private def fade(scene: Node, from: Double, to: Double): Unit = {
      runLater(() => {
        val fadeIn = new FadeTransition(Duration.millis(FADE_DURATION), scene)
        fadeIn.setFromValue(from)
        fadeIn.setToValue(to)
        fadeIn.play()
      })
    }
  }
}
