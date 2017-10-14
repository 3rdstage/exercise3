package thirdstage.exercise.akka.wordstats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import scala.concurrent.duration.Duration;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJobFailed;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsResult;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StatsAggregator extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final int expectedResults;
   private final ActorRef replyTo;
   private final List<Integer> results = new ArrayList<Integer>();

   public StatsAggregator(@Min(1) int expectedResults, ActorRef replyTo){
      this.expectedResults = expectedResults;
      this.replyTo = replyTo;
   }

   @Override
   public void preStart(){
      this.getContext().setReceiveTimeout(Duration.create(3, TimeUnit.SECONDS));
   }

   @Override
   public void onReceive(Object msg) throws Exception{

      if(msg instanceof Integer){
         Integer count = (Integer)msg;
         results.add(count);
         if(results.size() == this.expectedResults){
            int sum = 0;
            for(int c : results){ sum += c; }
            double mean = ((double)sum)/this.expectedResults;
            replyTo.tell(new StatsResult(mean), this.getSelf());
            this.getContext().stop(this.getSelf());
         }
      }else if(msg == ReceiveTimeout.getInstance()){
         replyTo.tell(new StatsJobFailed("Service unavailable, try again later."), this.getSelf());
         this.getContext().stop(this.getSelf());
      }else{
         this.unhandled(msg);
      }


   }

}
