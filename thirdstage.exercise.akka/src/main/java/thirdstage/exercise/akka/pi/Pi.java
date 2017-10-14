package thirdstage.exercise.akka.pi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;



/**
 * @author Sangmoon Oh
 * @see <a href="http://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-java.html">Getting Started Tutorial (Java): First Chapter</a>
 */
public class Pi {


   public void calculate(final int workers, final int elements, final int messages){
      ActorSystem system = ActorSystem.create("PiSystem");

      final ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");
      ActorRef master = system.actorOf(Props.create(Master.class, workers, elements, messages, listener), "master");

      master.tell(new Calculate(), ActorRef.noSender());

   }

}
