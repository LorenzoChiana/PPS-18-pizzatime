package utilities

/** Represents the window size for the Menu and the Game. */
object WindowSize extends Enumeration {
  case class SizeVal(width: Double, height: Double) extends super.Val

  val Menu: SizeVal = SizeVal(1005, 720)
  val Game: SizeVal = SizeVal(1005, 670)
}
