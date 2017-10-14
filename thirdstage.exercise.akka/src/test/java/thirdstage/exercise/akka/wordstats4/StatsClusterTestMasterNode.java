package thirdstage.exercise.akka.wordstats4;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.MDC;

public class StatsClusterTestMasterNode{

   public static void main(String[] args) throws Exception{

      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      StatsClusterMasterNode master = new StatsClusterMasterNode(
            StatsClusterMasterNode.MASTER_NODE_NETTY_PORT_DEFAULT,
            StatsClusterMasterNode.HTTP_PORT_DEFAULT,
            StatsClusterMasterNode.MQ_CLIENT_CONNECTOR_PORT_DEFAULT,
            StatsClusterMasterNode.MQ_JMX_REMOTE_PORT_DEFAULT,
            StatsClusterMasterNode.MQ_ADMIN_PORT_DEFAULT,
            "wordstats");

      master.setPID(pid);
      master.start(false);
   }

}
