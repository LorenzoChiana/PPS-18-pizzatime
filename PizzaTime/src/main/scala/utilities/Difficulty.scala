package utilities

import scala.language.implicitConversions
import gamelogic.Collectible
import gamelogic.Entity
import gamelogic.GameState._

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
    maxLife = 8,
    bonusScore = 40,
    obstacleDimension = Range(1, 2),
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
    obstacleDimension = Range(1, 3),
    levelThreshold = 5
  )
  val Hard: DifficultyVal = DifficultyVal(
    arenaWidth = 27,
    arenaHeight = 18,
    bonusRange = Range(3, 5),
    malusRange = Range(12, 20),
    bonusProb = 0.3,
    maxLife = 3,
    bonusScore = 20,
    obstacleDimension = Range(1, 3),
    levelThreshold = 3
  )
  val Extreme: DifficultyVal = DifficultyVal(
    arenaWidth = 36,
    arenaHeight = 24,
    bonusRange = Range(1, 3),
    malusRange = Range(24, 40),
    bonusProb = 0.1,
    maxLife = 1,
    bonusScore = 10,
    obstacleDimension = Range(1, 3),
    levelThreshold = 1
  )
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)

  /** Each difficulty will have a set of values that will affect the game.
   *
   *  @param arenaWidth the width of the [[Arena]]
   *  @param arenaHeight the height of the [[Arena]]
   *  @param bonusRange the [[Range]] of number of bonus [[Entity]]s (i.e. [[Collectible]]s)
   *  @param malusRange the [[Range]] of number of malus [[Entity]]s (i.e. [[Enemy]]s or [[Obstacle]]s)
   *  @param bonusProb the probability of having bonuses
   *  @param maxLife a value that represents the maximum number of lives
   *  @param bonusScore the score given by the score-related [[Collectible]]s
   *  @param obstacleDimension the dimension of the [[Obstacle]]s
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

  /** Implicit conversion from [[Value]] to [[DifficultyVal]].
   *
   *  @param value the [[Value]] to convert
   *  @return the converted [[DifficultyVal]]
   */
  implicit def valueToDifficultyVal(value: Value): DifficultyVal = value.asInstanceOf[DifficultyVal]
}