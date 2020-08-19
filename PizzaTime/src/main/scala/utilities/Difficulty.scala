package utilities

import scala.language.implicitConversions
import gamelogic.Collectible
import gamelogic.Enemy
import gamelogic.Obstacle

/** Represents the various difficulties that the game can have.
 *  The options are: easy, medium, hard or extreme.
 */
object Difficulty extends Enumeration {
  val Easy: DifficultyVal = DifficultyVal(
    arenaWidth = 12,
    arenaHeight = 8,
    bonusRange = Range(3, 5),
    malusRange = Range(1, 3),
    bonusProb = 0.8,
    maxLife = 5,
    bonusScore = 40,
    obstacleDimension = Range(1, 3),
    levelThreshold = 8
  )
  val Medium: DifficultyVal = DifficultyVal(
    arenaWidth = 18,
    arenaHeight = 12,
    bonusRange = Range(2, 5),
    malusRange = Range(6, 8),
    bonusProb = 0.5,
    maxLife = 5,
    bonusScore = 30,
    obstacleDimension = Range(1, 4),
    levelThreshold = 5
  )
  val Hard: DifficultyVal = DifficultyVal(
    arenaWidth = 27,
    arenaHeight = 18,
    bonusRange = Range(3, 5),
    malusRange = Range(12, 20),
    bonusProb = 0.3,
    maxLife = 5,
    bonusScore = 20,
    obstacleDimension = Range(1, 5),
    levelThreshold = 3
  )
  val Extreme: DifficultyVal = DifficultyVal(
    arenaWidth = 36,
    arenaHeight = 24,
    bonusRange = Range(1, 3),
    malusRange = Range(24, 40),
    bonusProb = 0.1,
    maxLife = 5,
    bonusScore = 10,
    obstacleDimension = Range(1, 6),
    levelThreshold = 1
  )
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)

  /** Each difficulty will have a set of values that will affect the game.
   *
   *  @param arenaWidth the width of the arena
   *  @param arenaHeight the height of the arena
   *  @param bonusRange the range of number of bonus entities (i.e. [[Collectible]]s)
   *  @param malusRange the range of number of malus entities (i.e. [[Enemy]]s or [[Obstacle]]s)
   *  @param bonusProb  probability of having bonuses
   *  @param maxLife a value that represents the maximum number of lives
   *  @param bonusScore the score given by the score-related [[Collectible]]s
   *  @param obstacleDimension the dimension of the [[Obstacle]]
   *  @param levelThreshold a threshold that affects level generation
   */
  case class DifficultyVal(arenaWidth: Int,
                           arenaHeight: Int,
                           bonusRange: Range,
                           malusRange: Range,
                           bonusProb: Double,
                           maxLife: Int,
                           bonusScore: Int,
                           obstacleDimension: Range,
                           levelThreshold: Int) extends super.Val

  /** Implicit conversion from a [[Value]] to a [[DifficultyVal]].
   *
   *  @param value the [[Value]] to convert
   *  @return the converted value
   */
  implicit def convert(value: Value): DifficultyVal = value.asInstanceOf[DifficultyVal]
}