package gamemanager

import java.io.File

import javax.sound.sampled.{AudioSystem, Clip}

import scala.language.postfixOps
import AudioSystem._
import Clip._
import utilities.{BonusSound, FailureSound, InjurySound, LevelMusic, LevelUp, MenuMusic, ShootSound, SoundType}

import scala.concurrent._
import ExecutionContext.Implicits.global

/** Allows to load and play the various sounds of the game.
 *  The sounds are for:
 *    Shooting
 *    Level music
 *    Failure
 *    Injury
 *    Bonus
 */
object SoundController {
  var gameClip: Clip = _

  /** Plays a sound.
   *
   *  @param sound the [[SoundType]] to be played
   */
  def play(sound: SoundType): Unit = sound match {
    case FailureSound => playSound(FailureSound path)
    case ShootSound => playSound(ShootSound path)
    case InjurySound => playSound(InjurySound path)
    case BonusSound => playSound(BonusSound path)
    case LevelMusic => playMusic(LevelMusic path)
    case MenuMusic => playMusic(MenuMusic path)
    case LevelUp => playSound(LevelUp path)
    case _ =>
  }

  /** Stops a looping sound. */
  def stopSound(): Future[Unit] = Future {gameClip.stop()}

  /** Plays a one-time sound.
   *
   *  @param path the path of the sound to be played
   */
  private def playSound(path: String): Future[Unit] = Future {
    val audioIn = getAudioInputStream(new File(path))
    val clip = getClip
    clip.open(audioIn)
    clip.start()
  }

  /** Plays a looping sound.
   *
   *  @param path the path of the sound to be played
   */
  private def playMusic(path: String): Future[Unit] = Future {
    val audioIn = getAudioInputStream(new File(path))
    gameClip = getClip
    gameClip.open(audioIn)
    gameClip.loop(LOOP_CONTINUOUSLY)
  }
}
