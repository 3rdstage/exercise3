package thirdstage.exercise.akka.swap;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

public class Swapper extends UntypedActor{
   LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);


   @Override
   public void onReceive(Object message) throws Exception{
      if(message == Swap.SWAP){
         this.logger.info("Hi");
      }else{
         this.unhandled(message);
      }
   }

   public static Props props(){
      return Props.create(new Creator<Swapper>(){

         private static final long serialVersionUID = 1L;

         @Override
         public Swapper create() throws Exception{
            return new Swapper();
         }

      });
   }


}
