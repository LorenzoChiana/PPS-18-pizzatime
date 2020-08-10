package utilities

/**
 * Represents the preferences that the user can sets
 * @param playerName the name of player
 * @param difficulty the difficulty of the game which can be selected from values of [[Difficulty]]
 */
case class SettingPreferences(playerName: String, difficulty: Difficulty.Value)
