package gameview

import javafx.animation.{Interpolator, Transition}
import javafx.application.Platform
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import javafx.util.Duration

class SpriteAnimation(imageView: ImageView, duration: Duration, count: Int, col: Int, var offsetX: Int, var offsetY: Int, width: Int, height: Int) extends Transition{
  var lastIndex: Int = 0
  Platform.runLater(() => {
    setCycleDuration(duration)
    setInterpolator(Interpolator.LINEAR)
    imageView.setViewport(new Rectangle2D (offsetX, offsetY, width, height))
  })

  protected def interpolate(k: Double): Unit = { //
    val index = Math.min(Math.floor(k * count).toInt, count - 1)
    if (index != lastIndex) {
      val x = (index % col) * width + offsetX
      val y = (index / col) * height + offsetY
      imageView.setViewport(new Rectangle2D(x, y, width, height))
      lastIndex = index
    }
  }
}
