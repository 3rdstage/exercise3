package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import org.slf4j.MDC;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorSystem;

public class MappedRouterActorTest1Worker{

   public static void main(String[] args) throws Exception{
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      String addr = InetAddress.getLocalHost().getHostAddress();

      Config config = ConfigFactory.load();
      config = config.getConfig("wordanalysis").withFallback(config);
      config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);
      config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + addr).withFallback(config);
      config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 2552).withFallback(config);

      ActorSystem system = ActorSystem.create("WordAnalysis", config);

      System.out.println("The master node of Akka cluster has started.\n"
            + "Type retun key to end this process.");
      System.in.read();

      system.shutdown();

   }

}
