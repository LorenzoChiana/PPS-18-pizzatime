package utilities

/**
 * Represents the various difficulties that the game may have.
 * The game can be: easy, medium, hard or extreme.
 */
object Difficulty extends Enumeration {
  val Easy, Medium, Hard, Extreme = Value
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)
}