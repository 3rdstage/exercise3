package thirdstage.exercise.akka.simplecluster;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SimpleClusterTest{

   private static String pid;
   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleClusterTest.class);

   public static void main(String... args){

      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      if(args.length == 0){
         startup(new String[] {"2551", "2552", "0"});
      }else{
         startup(args);
      }

   }

   private static void startup(String... ports){
      Config config = ConfigFactory.load();
      config = config.getConfig("simplecluster").withFallback(config);

      for(String port : ports){
         final ActorSystem system = ActorSystem.create("ClusterSystem",
               ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(config));

         system.actorOf(Props.create(SimpleClusterListener.class), "clusterListener");

         system.registerOnTermination(new Runnable(){
            @Override
            public void run(){
               logger.info("The actor system at the process of {} is terminating", pid);
            }
         });

         Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
               system.shutdown();
            }
         });
      }
   }

}
