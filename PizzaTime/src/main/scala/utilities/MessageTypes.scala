package utilities

/** Types of dialog messages. */
object MessageTypes {
  sealed trait MessageType
  case object Info extends MessageType
  case object Error extends MessageType
  case object Warning extends MessageType
}
