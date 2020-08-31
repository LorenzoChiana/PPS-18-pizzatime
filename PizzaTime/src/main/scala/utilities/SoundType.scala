package utilities

import java.lang.System.getProperty

  /** Allows to load the various sounds of the game.
   *  The sounds are for:
   *    Shooting
   *    Level music
   *    Failure
   *    Injury
   *    Bonus
   *    LevelUp
   */

  sealed trait SoundType
  case object ShootSound extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/shoot.wav"}
  case object LevelMusic extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/levelTrack.wav"}
  case object FailureSound extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/lose.wav"}
  case object InjurySound extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/die.wav"}
  case object BonusSound extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/bonus.wav"}
  case object MenuMusic extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/menu.mp3"}
  case object LevelUp extends SoundType {
    val path: String = getProperty("user.dir")+"/src/main/resources/sounds/levelUp.mp3"}


