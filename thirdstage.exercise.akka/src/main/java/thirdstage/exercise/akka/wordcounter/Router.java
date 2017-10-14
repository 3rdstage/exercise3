package thirdstage.exercise.akka.wordcounter;

import java.util.HashMap;
import java.util.Map;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Router extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final Map<String, ActorRef> counters = new HashMap<String, ActorRef>();

   public Router(){

      char initial = 0x41;
      for( ; initial < 0x5B; initial++){
         counters.put(String.valueOf(initial),
               this.getContext().actorOf(Props.create(WordCounter.class, initial), "WordCounter" + initial));
         this.logger.info("An actor[name: {}] is created.", "WordCounter" + initial);
      }

   }

   @Override
   public void onReceive(Object message){

      if(message instanceof String && ((String)message).length() > 0){
         String first = ((String)message).substring(0, 1);
         ActorRef counter = this.counters.get(first);
         if(counter != null){
            counter.tell(message, getSelf());
         }else{
            this.unhandled(message);
         }
      }else{
         this.unhandled(message);
      }
   }
}
