package utilities

import javafx.animation.FadeTransition
import javafx.application.Platform
import javafx.scene.Node
import javafx.util.Duration

object Animations {
  val FADE_DURATION = 400
  val OPACITY_FADE_IN = 0
  val OPACITY_FADE_OUT = 1

  object Fade {

    private def fade(scene: Node, from: Double, to: Double): Unit = {
      Platform.runLater(() => {
        val fadeIn = new FadeTransition(Duration.millis(FADE_DURATION), scene)
        fadeIn.setFromValue(from)
        fadeIn.setToValue(to)
        fadeIn.play()
      })
    }

    def fadeIn(scene: Node): Unit = {
      require(scene != null)
      scene.setOpacity(OPACITY_FADE_IN)
      fade(scene, 0, 1)
    }

    def fadeOut(scene: Node): Unit = {
      require(scene != null)
      scene.setOpacity(OPACITY_FADE_OUT)
      fade(scene, 1, 0)
    }
  }
}
