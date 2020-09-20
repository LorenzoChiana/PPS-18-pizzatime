package utilities

import gameview.scene.SceneType

/** Represents an intent to change the current scene with a new one.
 *
 *  @param sceneType the new type of scene that will replace the old one
 */
class Intent (val sceneType: SceneType.Value)
