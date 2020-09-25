package utilities

/** A simple logger for debugging purposes. */
object Logger {
  val prefix = "[LOG] "

  def log(s: String): Unit = {
    println(prefix + s)
  }
}