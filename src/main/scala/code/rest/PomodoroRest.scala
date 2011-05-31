package pomodoro

import net.liftweb.http._
import net.liftweb.http.rest._
import code.comet._

object PomodoroRest extends RestHelper {

  serve {
    case Req("api" :: "pomodoroBegun" :: duration :: _, "xml", GetRequest) =>
      val endTime = System.currentTimeMillis + java.lang.Long.parseLong(duration)
      PomodoroServer ! PomodoroBegun(endTime)
       <html><body>Pomodoro started</body></html>
    case Req("api" :: "pomodoroAborted" :: _, "xml", GetRequest) =>
      PomodoroServer ! PomodoroAborted
       <html><body>Pomodoro aborted</body></html>
  }

}

