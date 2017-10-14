package thirdstage.exercise.akka.wordanalysis;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ClusterTestMaster {

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClusterTestMaster.class);

   public static void main(String[] args) throws Exception{
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      try{
         ClusterMaster master = new ClusterMaster(
               "WordAnalysis", "wordanalysis", ClusterMaster.MASTER_NODE_NETTY_PORT_DEFAULT,
               ClusterMaster.HTTP_PORT_DEFAULT, "wordanalysis");

         master.setPID(pid);
         master.start(false);

         System.out.println("The master node of Akka cluster has started.\nType return key to exit");
         System.in.read();

         master.stop();


      }catch(Exception ex){
         logger.error("Fail to run cluster master node.", ex);
         throw new RuntimeException("Fail to run cluster master node.", ex);
      }
   }

}
