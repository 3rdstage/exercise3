package thirdstage.exercise.akka.swap;

import org.testng.annotations.Test;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SwapperTest {

   @Test
   public void test1() {
      ActorSystem system = ActorSystem.create("MySystem");
      ActorRef swapper = system.actorOf(Props.create(Swapper.class));

      swapper.tell(Swap.SWAP, ActorRef.noSender());
      swapper.tell(Swap.SWAP, ActorRef.noSender());
   }
}
