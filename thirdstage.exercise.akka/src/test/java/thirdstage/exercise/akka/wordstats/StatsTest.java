package thirdstage.exercise.akka.wordstats;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJob;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class StatsTest{

   public static final String ACTOR_SYSTEM_NAME_DEFAULT = "WordStats";

   public static final String APPL_NAME_DEFAULT = ACTOR_SYSTEM_NAME_DEFAULT.toLowerCase();

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StatsTest.class);

   private static String pid;

   private static int count = 1;

   public static void main(String[] args){

      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

      final String sentence = System.getProperty("sentence",
            "She's got a smile that it seems to me remindes me of childhood memories.");

      if(args.length == 0){
         startup(new String[]{"2551", "2552", "0"}, sentence);
      }else{
         startup(args, sentence);
      }
   }

   private static void startup(final String[] ports, final String sentence){

      Config config = ConfigFactory.load();
      config = config.getConfig(APPL_NAME_DEFAULT).withFallback(config);
      config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);

      for(String port : ports){
         logger.info("Starting the actor system. - Netty Port: {}", port);

         final ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME_DEFAULT,
               ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(config));

         system.actorOf(Props.create(StatsWorker.class), "statsWorker");
         ActorRef service = system.actorOf(Props.create(StatsService.class), "statsService");

         service.tell(new StatsJob(String.valueOf(count++), sentence), service);

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
