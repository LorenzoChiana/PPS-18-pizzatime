package gameview

import javafx.animation.{Interpolator, Transition}
import javafx.application.Platform
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import javafx.util.Duration

/**Used to animate the [[Player]].
 *
 * @param imageView imageview of the player
 * @param duration duration of the animation
 * @param count number of frames
 * @param col number of frames in one row in the image
 * @param offsetX the X offset to set the Viewport
 * @param offsetY the Y offset to set the Viewport
 * @param width the width of the Viewport
 * @param height the height of the Viewport
 */

class SpriteAnimation(
  imageView: ImageView,
  duration: Duration,
  count: Int,
  col: Int,
  var offsetX: Int,
  var offsetY: Int,
  width: Int,
  height: Int
) extends Transition {

  var lastIndex: Int = 0

  Platform.runLater(() => {
    setCycleDuration(duration)
    setInterpolator(Interpolator.LINEAR)
    imageView.setViewport(new Rectangle2D (offsetX, offsetY, width, height))
  })

  protected def interpolate(k: Double): Unit = {
    val index = Math.min(Math.floor(k * count).toInt, count - 1)
    if (index != lastIndex) {
      val x = (index % col) * width + offsetX
      val y = (index / col) * height + offsetY
      imageView.setViewport(new Rectangle2D(x, y, width, height))
      lastIndex = index
    }
  }
}
