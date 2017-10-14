package thirdstage.exercise.akka.wordstats2;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class StatsMasterMain{

   public static final String ACTOR_SYSTEM_NAME_DEFAULT = "WordStats";

   public static final String APPL_NAME_DEFAULT = ACTOR_SYSTEM_NAME_DEFAULT.toLowerCase();

   public static void main(String... args){

   }

   public static void startup(String[] ports){

      Config config = ConfigFactory.load();
      config = config.getConfig(APPL_NAME_DEFAULT).withFallback(config);
      config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);


      for(String port : ports){
         ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(config);
         ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME_DEFAULT, config);

         //ClusterSingletonManagerSettings settings = ClusterSingletonManagerSettings.create(system).withRole("compute");

      }

   }


}
