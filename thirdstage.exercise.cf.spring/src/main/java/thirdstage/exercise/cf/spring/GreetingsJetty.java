package thirdstage.exercise.cf.spring;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GreetingsJetty{

   public static void main(String[] args) throws Exception{
      int port = Integer.valueOf(System.getenv("PORT"));
      Server server = new Server(port);
      server.setHandler(new AbstractHandler(){
         @Override
         public void handle(String target, Request baseReq, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            baseReq.setHandled(true);
            resp.getWriter().println("<h1>Hello, world</h1>");
         }
      });

      server.start();
      server.join();

   }

}
