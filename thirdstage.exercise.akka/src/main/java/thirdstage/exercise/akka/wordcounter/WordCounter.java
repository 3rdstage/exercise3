package thirdstage.exercise.akka.wordcounter;

import javax.annotation.Nonnull;
import javax.validation.constraints.Size;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WordCounter extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final String initial;

   public String getInitial(){ return this.initial; }

   private long count;

   public long getCount(){ return this.count; }

   public WordCounter(@Nonnull final char initial){
      this.initial = String.valueOf(initial);
   }

   public WordCounter(@Nonnull @Size(min=1, max=1) final String initial){
      this.initial = initial;
   }


   @Override
   public void onReceive(Object message){
      if(message instanceof String && ((String)message).startsWith(this.initial)){
         this.count++;
         this.logger.debug("Received a word starting with {}, the counter for {} is {}", this.initial, this.initial, count);
      }else{
         unhandled(message);
      }
   }



}
