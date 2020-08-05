package gameview.scene

import gameview.ObservableView
import gameview.observer.SettingsSceneObserver
import utilities.SettingPreferences

/**
 * Models the settings menu scene
 */
trait SettingsScene extends ObservableView[SettingsSceneObserver] with Scene {
  def showCurrentPreferences(settingPreferences: SettingPreferences)
}
