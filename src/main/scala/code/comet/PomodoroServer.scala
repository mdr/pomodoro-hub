package code.comet

import java.util.Date
import java.text.DateFormat
import net.liftweb._
import net.liftweb.http.ListenerManager
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds._
import net.liftweb.actor.LiftActor

case class PomodoroInfo(targetTimeOpt: Option[Long], summonses: List[String])

sealed trait PomodoroMessage

case class PomodoroBegun(targetTime: Long) extends PomodoroMessage

case object PomodoroAborted extends PomodoroMessage

case class AddSummons(message: String) extends PomodoroMessage

case object ClearAllSummons extends PomodoroMessage

object PomodoroServer extends LiftActor with ListenerManager {

  private var targetTimeOpt: Option[Long] = None
  private var summonses: List[String] = Nil

  protected def createUpdate = PomodoroInfo(targetTimeOpt, summonses)

  override protected def lowPriority = {
    case PomodoroBegun(targetTime) =>
      targetTimeOpt = Some(targetTime)
      updateListeners()
    case PomodoroAborted =>
      targetTimeOpt = None
      updateListeners()
    case AddSummons(m) =>
      summonses = ("[" + nowString + "] " + m) :: summonses
      updateListeners()
    case ClearAllSummons =>
      summonses = Nil
      updateListeners()
  }

  private def nowString = DateFormat.getTimeInstance format new Date

}
