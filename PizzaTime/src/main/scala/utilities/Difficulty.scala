package utilities

import scala.language.implicitConversions
import gamelogic.Collectible
import gamelogic.Arena
import gamelogic.Enemy
import gamelogic.Obstacle

/** Represents the various difficulties that the game can have.
 *  The options are: easy, medium, hard or extreme.
 */
object Difficulty extends Enumeration {
  val Easy: DifficultyVal = DifficultyVal(
    arenaWidth = 12,
    arenaHeight = 8,
    enemiesRange = Range(1, 3),
    collectiblesRange = Range(3, 5),
    obstaclesRange = Range(1, 3),
    maxLife = 8,
    bonusScore = 40,
    obstacleDimension = Range(1, 2),
    levelThreshold = 8
  )
  val Medium: DifficultyVal = DifficultyVal(
    arenaWidth = 18,
    arenaHeight = 12,
    enemiesRange = Range(5, 8),
    collectiblesRange = Range(3, 5),
    obstaclesRange = Range(5, 8),
    maxLife = 5,
    bonusScore = 30,
    obstacleDimension = Range(1, 3),
    levelThreshold = 5
  )
  val Hard: DifficultyVal = DifficultyVal(
    arenaWidth = 27,
    arenaHeight = 18,
    enemiesRange = Range(10, 20),
    collectiblesRange = Range(3, 5),
    obstaclesRange = Range(8, 12),
    maxLife = 3,
    bonusScore = 20,
    obstacleDimension = Range(1, 4),
    levelThreshold = 3
  )
  val Extreme: DifficultyVal = DifficultyVal(
    arenaWidth = 36,
    arenaHeight = 24,
    enemiesRange = Range(20, 30),
    collectiblesRange = Range(1, 3),
    obstaclesRange = Range(12, 16),
    maxLife = 1,
    bonusScore = 10,
    obstacleDimension = Range(1, 5),
    levelThreshold = 1
  )
  val allDifficulty = Seq(Easy, Medium, Hard, Extreme)

  /** Each difficulty will have a set of values that will affect the game.
   *
   *  @param arenaWidth the width of the [[Arena]]
   *  @param arenaHeight the height of the [[Arena]]
   *  @param enemiesRange the [[Range]] of number of [[Enemy]]s
   *  @param collectiblesRange the [[Range]] of number of [[Collectible]]s
   *  @param obstaclesRange the [[Range]] of number of [[Obstacle]]s
   *  @param maxLife a value that represents the maximum number of lives
   *  @param bonusScore the score given by the score-related [[Collectible]]s
   *  @param obstacleDimension the [[Range]] of dimension of the [[Obstacle]]s
   *  @param levelThreshold a threshold that affects level generation
   */
  case class DifficultyVal(arenaWidth: Int,
                           arenaHeight: Int,
                           enemiesRange: Range,
                           collectiblesRange: Range,
                           obstaclesRange: Range,
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