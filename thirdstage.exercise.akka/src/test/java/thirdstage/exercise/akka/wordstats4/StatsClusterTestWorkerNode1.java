package thirdstage.exercise.akka.wordstats4;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import org.slf4j.MDC;

public class StatsClusterTestWorkerNode1{


   public static void main(String[] args) throws Exception{

      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      StatsClusterWorkerNode worker = new StatsClusterWorkerNode(
            StatsClusterMasterNode.MASTER_NODE_NETTY_PORT_DEFAULT + 1,
            InetAddress.getLocalHost().getHostAddress(),
            StatsClusterMasterNode.MQ_CLIENT_CONNECTOR_PORT_DEFAULT,
            "wordstats");

      worker.setPID(pid);
      worker.start(false);
   }

}
