package thirdstage.exercise.akka.pi;

import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;

public class Master extends UntypedActor{

   private final int numOfMessages;

   private final int numOfElements;

   private double pi;

   private int numOfResults = 0;

   private final long start = System.currentTimeMillis();

   private final ActorRef listener;

   private final ActorRef router;


   public Master(final int workers, final int messages, final int elements, ActorRef listener){
      this.numOfMessages = messages;
      this.numOfElements = elements;

      this.listener = listener;

      this.router = this.getContext().actorOf(new RoundRobinPool(this.numOfElements).props(Props.create(Worker.class)), "workerRouter");
   }

   @Override
   public void onReceive(Object msg) throws Exception{
      if(msg instanceof Calculate){
         for (int start = 0; start < this.numOfMessages; start++){
            this.router.tell(new Work(start, this.numOfElements), this.getSelf());
         }
      }else if(msg instanceof Result){
         Result result = (Result) msg;
         pi += result.getValue();
         this.numOfResults += 1;
         if(this.numOfResults == this.numOfMessages){
            Duration duration = Duration.create(System.currentTimeMillis() - this.start, TimeUnit.MILLISECONDS);
            listener.tell(new PiApproximation(pi, duration), this.getSelf());
            this.getContext().stop(this.getSelf());
         }else{
            this.unhandled(msg);
         }
      }
   }

   public static Props props(final int workers, final int messages, final int elements, final ActorRef listener){
      return Props.create(new Creator<Master>(){
         private static final long serialVersionUID = 1L;

         @Override
         public Master create() throws Exception{
            return new Master(workers, messages, elements, listener);
         }
      });
   }

}
