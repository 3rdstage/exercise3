package thirdstage.exercise.akka.wordstats;

import java.util.HashMap;
import java.util.Map;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsTask;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StatsWorker extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final Map<String, Integer> cache = new HashMap<>();

   @Override
   public void onReceive(Object msg){
      if(msg instanceof StatsTask){
         StatsTask task = (StatsTask)msg;
         String word = task.getWord();
         this.logger.debug("Received a task - Job ID: {}, Task No: {}, Word: '{}'", task.getJobId(), task.getTaskNo(), word);

         Integer len = cache.get(word);
         if(len == null){
            len = word.length();
            cache.put(word, len);
         }
         getSender().tell(len, getSelf());

      }else{
         this.unhandled(msg);
      }
   }

}
