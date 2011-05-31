package code.comet

case class Message(user: String, msg: String)

case class MessageLog(messages: List[Message])
