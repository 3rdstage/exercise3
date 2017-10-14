package thirdstage.exercise.akka.wordanalysis;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ClusterTestWorker1{

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClusterTestMaster.class);

   public static void main(String[] args) throws Exception{
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      try{
         ClusterWorker worker = new ClusterWorker(
               "WordAnalysis", "wordanalysis", ClusterMaster.MASTER_NODE_NETTY_PORT_DEFAULT + 1,
               "wordanalysis");

         worker.setPID(pid);
         worker.start(false);

      }catch(Exception ex){
         logger.error("Fail to run cluster master node.", ex);
         throw new RuntimeException("Fail to run cluster master node.", ex);
      }
   }


}
