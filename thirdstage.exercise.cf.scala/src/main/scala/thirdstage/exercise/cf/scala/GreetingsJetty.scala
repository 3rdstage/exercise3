package thirdstage.exercise.cf.scala

import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.Request

object GreetingsJetty {

  //val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]) {

    val port = Integer.valueOf(System.getenv("PORT"))
    val server = new org.eclipse.jetty.server.Server(port)

    server.setHandler(new AbstractHandler() {

      override def handle(target: String, baseReq: Request, req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setContentType("text/html;charset=utf-8")
        resp.setStatus(HttpServletResponse.SC_OK)
        baseReq.setHandled(true)
        resp.getWriter().println("<h1>Greetings by Jetty in Scala on Bluemix")
      }
    });
  }
}