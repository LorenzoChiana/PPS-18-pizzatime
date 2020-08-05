package gameview.observer

import gameview.controller.SceneController
import gameview.scene.SettingsScene
import utilities.SettingPreferences

trait SettingsSceneObserver extends ViewObserver with SceneController[SettingsScene] {
  def onBack(): Unit
  def onApply(settingPreferences: SettingPreferences): Unit
  def init(): Unit
}
