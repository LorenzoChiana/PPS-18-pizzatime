package utilities

sealed trait ActionType
case object Shoot extends ActionType
case object Movement extends ActionType
case object TakeCollectible extends ActionType

case class Action(actionType: ActionType, direction: Option[Direction])
