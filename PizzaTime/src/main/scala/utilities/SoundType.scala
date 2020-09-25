package utilities

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
  case object ShootSound extends SoundType {val path: String = "/sounds/shoot.wav"}
  case object LevelMusic extends SoundType {val path: String = "/sounds/levelTrack.wav"}
  case object FailureSound extends SoundType {val path: String = "/sounds/lose.wav"}
  case object InjurySound extends SoundType {val path: String = "/sounds/die.wav"}
  case object BonusSound extends SoundType {val path: String = "/sounds/bonus.wav"}
  case object MenuMusic extends SoundType {val path: String = "/sounds/menu.wav"}
  case object LevelUpSound extends SoundType {val path: String = "/sounds/levelUp.wav"}
  case object EnemyInjurySound extends SoundType {val path: String = "/sounds/enemyDie.wav"}


