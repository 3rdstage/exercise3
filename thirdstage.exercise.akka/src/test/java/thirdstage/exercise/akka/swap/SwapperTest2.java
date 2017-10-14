package thirdstage.exercise.akka.swap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SwapperTest2{

   public static void main(String... args) {

      Config config = ConfigFactory.load();
      ActorSystem system = ActorSystem.create("MySystem", config.getConfig("swap").withFallback(config));
      //ActorRef swapper = system.actorOf(Swapper.props(), "Swapper"); // Causes runtime exception
      ActorRef swapper = system.actorOf(Props.create(Swapper.class));

      swapper.tell(Swap.SWAP, ActorRef.noSender());
      swapper.tell(Swap.SWAP, ActorRef.noSender());
   }

}
