package utilities

/**
 * Represents the various difficulties that the game may have.
 * The game can be: easy, medium, hard or extreme.
 */
object Difficulty extends Enumeration {
  val Easy: DifficultyVal = DifficultyVal(
    bonusProb = 0.8,
    malusProb = 0.2,
    arenaWidth = 16,
    arenaHeight = 9
  )
  val Medium: DifficultyVal = DifficultyVal(
    bonusProb = 0.5,
    malusProb = 0.5,
    arenaWidth = 32,
    arenaHeight = 18
  )
  val Hard: DifficultyVal = DifficultyVal(
    bonusProb = 0.3,
    malusProb = 0.7,
    arenaWidth = 64,
    arenaHeight = 36
  )
  val Extreme: DifficultyVal = DifficultyVal(
    bonusProb = 0.1,
    malusProb = 0.9,
    arenaWidth = 128,
    arenaHeight = 72
  )
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)

  case class DifficultyVal(bonusProb: Double,
                           malusProb: Double,
                           arenaWidth: Int,
                           arenaHeight: Int) extends super.Val

  implicit def convert(value: Value): DifficultyVal = value.asInstanceOf[DifficultyVal]
}
