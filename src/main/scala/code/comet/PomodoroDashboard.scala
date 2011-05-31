package code.comet
import net.liftweb._
import http._
import net.liftweb.http.SHtml._
import net.liftweb.http.SHtml
import net.liftweb.common.{ Box, Full }
import net.liftweb.util._
import net.liftweb.actor._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{ SetHtml }
import net.liftweb.http.js.JE.Str
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JsCmds
import scala.xml._

class PomodoroDashboard extends CometActor with CometListener {

  private var pomodoroInfoOpt: Option[PomodoroInfo] = None

  protected val registerWith = PomodoroServer

  def render = pomodoroInfoOpt match {
    case Some(pomodoroInfo) =>
      val targetTimeOpt = pomodoroInfo.targetTimeOpt
      val remaining = targetTimeOpt map { _ - System.currentTimeMillis } getOrElse -1
      val setTargetTimeCommand = Run("setTargetTime('" + remaining + "', " + targetTimeOpt.isDefined + ");")
      renderSummons(pomodoroInfo.summonses) ++ setTargetTimeCommand

    case None =>
      renderSummons(summonses = Nil)
  }

  private def renderSummons(summonses: List[String]): RenderOut = {
    val deleteAllSummonsButton = ajaxButton("Delete all summons") { PomodoroServer ! ClearAllSummons }
    val summonMessageField = ajaxForm(text { s => PomodoroServer ! AddSummons(s) } ++ ajaxSubmit("Summon Matt"))
    val summonsList = summonses map { summons => <li>{ summons }</li> }
    bind("pomodoro",
      "summonses" -> summonsList,
      "summonMessageField" -> summonMessageField,
      "deleteAllSummons" -> deleteAllSummonsButton)
  }

  
  override def lowPriority = {
    case pomodoroInfo: PomodoroInfo =>
      this.pomodoroInfoOpt = Some(pomodoroInfo)
      reRender(false)
  }

  override val devMode = true

  private def ajaxButton(label: String)(proc: => Unit) = SHtml.ajaxButton(label, () => { proc; Noop })
  private def text(proc: String => Unit) = SHtml.text("", s => { proc(s); Noop })
  private def ajaxSubmit(label: String) = SHtml.ajaxSubmit(label, () => Noop)
}

