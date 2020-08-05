package gameview.controller

import gameview.observer.SettingsSceneObserver
import gameview.scene.SceneType
import utilities.{Intent, MessageTypes, SettingPreferences}
import gamemanager.handlers.PreferencesHandler

case class SettingsSceneController() extends SettingsSceneObserver {
  override def init(): Unit = view.foreach(view => view.showCurrentPreferences(SettingPreferences(
    PreferencesHandler.getName,
    PreferencesHandler.getDifficulty
  )))

  override def onBack(): Unit = {
    require(view.isDefined)
    view.get.windowManager.scene_(new Intent(SceneType.MainScene))
  }

  override def onApply(settingPreferences: SettingPreferences): Unit = {
    require(view.isDefined)

    PreferencesHandler.setName(settingPreferences.playerName)
    PreferencesHandler.setDifficulty(settingPreferences.difficulty)

    view.get.windowManager.showMessage("Impostazioni salvate con successo", MessageTypes.Info)
  }
}
