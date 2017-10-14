package thirdstage.exercise.akka.wordcounter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class WordCounterTest{

   public static void main(String... args) {

      Config config = ConfigFactory.load();
      ActorSystem system;
      if(config.hasPath("wordcounter")){
         system = ActorSystem.create("WordCounterCluster", config.getConfig("wordcounter").withFallback(config));
      }else{
         system = ActorSystem.create("WordCounterCluster");
      }
      ActorRef router = system.actorOf(Props.create(Router.class));


      router.tell("Word", ActorRef.noSender());


   }

}
