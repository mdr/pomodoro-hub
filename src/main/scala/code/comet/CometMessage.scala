package code.comet
import scala.xml._
import net.liftweb._
import http._
import SHtml._
import net.liftweb.common.{ Box, Full }
import net.liftweb.util._
import net.liftweb.actor._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{ SetHtml }
import net.liftweb.http.js.JE.Str

class CometMessage extends CometActor {

  override def defaultPrefix = Full("comet")

  def render = bind("comet", "message" -> <span id="message">Whatever you feel like returnin</span>)

  ActorPing.schedule(this, Message, 10000L)

  override def lowPriority = {
    case Message ⇒
      partialUpdate(SetHtml("message", Text("updated: " + timeNow.toString)))
      ActorPing.schedule(this, Message, 10000L)
  }

}

