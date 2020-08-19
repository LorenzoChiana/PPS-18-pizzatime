package utilities

import scala.language.implicitConversions

/** Represents the various difficulties that the game can have.
 *  The options are: easy, medium, hard or extreme.
 */
object Difficulty extends Enumeration {
  val Easy: DifficultyVal = DifficultyVal(
    bonusProb = 0.8,
    malusRange = Range(3, 5),
    arenaWidth = 16,
    arenaHeight = 9,
    bonusRange = Range(3, 5),
    levelThreshold = 5
  )
  val Medium: DifficultyVal = DifficultyVal(
    bonusProb = 0.5,
    malusRange = Range(3, 5),
    arenaWidth = 32,
    arenaHeight = 18,
    bonusRange = Range(2, 5),
    levelThreshold = 5
  )
  val Hard: DifficultyVal = DifficultyVal(
    bonusProb = 0.3,
    malusRange = Range(3, 5),
    arenaWidth = 64,
    arenaHeight = 36,
    bonusRange = Range(3, 5),
    levelThreshold = 5
  )
  val Extreme: DifficultyVal = DifficultyVal(
    bonusProb = 0.1,
    malusRange = Range(3, 5),
    arenaWidth = 128,
    arenaHeight = 72,
    bonusRange = Range(1, 5),
    levelThreshold = 5
  )
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)

  /** Each difficulty will have a set of values that will affect the game.
   *
   *  @param arenaWidth the width of the arena
   *  @param arenaHeight the height of the arena
   *  @param bonusProb  probability of having bonuses
   *  @param levelThreshold a threshold that affects level generation
   *  @param bonusRange the range of number of bonus entities (i.e. Collectibles)
   *  @param malusRange the range of number of malus entities (i.e. Enemies or Obstacles)
   */
  case class DifficultyVal(arenaWidth: Int,
                           arenaHeight: Int,
                           bonusProb: Double,
                           levelThreshold: Int,
                           bonusRange: Range,
                           malusRange: Range,
                           ) extends super.Val

  /** Implicit conversion from a [[Value]] to a [[DifficultyVal]].
   *
   *  @param value the [[Value]] to convert
   *  @return the converted value
   */
  implicit def convert(value: Value): DifficultyVal = value.asInstanceOf[DifficultyVal]
}