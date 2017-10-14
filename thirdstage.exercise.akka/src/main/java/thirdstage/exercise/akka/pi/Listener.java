package thirdstage.exercise.akka.pi;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Listener extends UntypedActor{
   LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   @Override
   public void onReceive(Object msg) throws Exception{
      if(msg instanceof PiApproximation){
         PiApproximation pi = (PiApproximation) msg;
         logger.info(String.format("Pi approximation : %s", pi.getPi()));
         logger.info(String.format("Calculation time : %s", pi.getDuration()));
      }else{
         this.unhandled(msg);
      }

   }

}
