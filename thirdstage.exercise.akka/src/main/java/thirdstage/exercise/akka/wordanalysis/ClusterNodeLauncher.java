package thirdstage.exercise.akka.wordanalysis;

import java.io.Console;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ClusterNodeLauncher{

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClusterNodeLauncher.class);

   public static void main(String[] args) throws Exception{
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      String pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      if(!(args.length == 2 || args.length == 3)){
         printUsage("There supposed to be 2 or 3 argument.");
         return;
      }

      String type = args[0];
      if(!"master".equals(type) && !"worker".equals(type)){
         printUsage("The type parameter is expected to be 'master' or 'worker'. But you entered '" + type + "'.");
         return;
      }

      int nettyPort = Integer.valueOf(args[1]);
      int httpPort = ClusterMaster2.HTTP_PORT_DEFAULT;
      if("master".equals(type) && args.length == 3){
         httpPort = Integer.valueOf(args[2]);
      }

      final ClusterNode node;
      try{

         if("master".equals(type)){
            node = new ClusterMaster2("WordAnalysis", "wordanalysis", nettyPort,
                  httpPort, "wordanalysis").setPID(pid);
         }else{
            node = new ClusterWorker("WordAnalysis", "wordanalysis", nettyPort,
                  "wordanalysis").setPID(pid);
         }

         node.start(false);
      }catch(Exception ex){
         logger.error("Fail to create or start {} node.", type, ex);
         throw ex;
      }

      if(node != null){
         Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
               try{ node.stop(); }
               catch(Exception ex){
                  logger.error("Fail to stop actor system.", ex);
               }
            }

         });
      }

   }

   private static void printUsage(String headline){

      Console con = System.console();

      if(StringUtils.isNotBlank(headline)){
         con.printf("\n");
         con.printf(headline);
         con.printf("\n");
      }

      con.printf("\n");
      con.printf("Syntax : > start-node.bat type port [http-port]\n");
      con.printf("\n");

      con.printf("  type : 'master' or 'worker', mandatory\n");
      con.printf("  port : TCP port for the node, mandatory\n");
      con.printf("  http-port : HTTP port for the master node to communicate with client, optional\n");


   }

}
