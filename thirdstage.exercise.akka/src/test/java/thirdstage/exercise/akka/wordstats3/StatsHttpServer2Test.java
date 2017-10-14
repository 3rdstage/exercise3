package thirdstage.exercise.akka.wordstats3;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.MDC;

public class StatsHttpServer2Test{


   public static void main(String[] args) throws Exception{

      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      int nettyPort = StatsHttpServer2.MASTER_NODE_NETTY_PORT_DEFAULT;
      int httpPort = StatsHttpServer2.HTTP_PORT_DEFAULT;
      String appName = StatsHttpServer2.APPL_NAME_DEFAULT;

      StatsHttpServer2 server = new StatsHttpServer2(nettyPort, httpPort, appName);

      server.start(true);
   }

}
