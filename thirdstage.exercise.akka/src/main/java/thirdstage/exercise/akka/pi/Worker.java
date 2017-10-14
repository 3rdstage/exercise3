package thirdstage.exercise.akka.pi;

import akka.actor.UntypedActor;

public class Worker extends UntypedActor{

   private double calculatePiFor(int start, int elements){
      double acc = 0.0;
      for(int i = start * elements; i < (start + 1) * elements; i++){
         acc += 4.0 * (1 - (i % 2) * 2)/(2 * i + 1);
      }
      return acc;
   }


   @Override
   public void onReceive(Object message) throws Exception{
      if(message instanceof Work){
         Work work = (Work) message;
         double result = this.calculatePiFor(work.getStart(), work.getNumOfElements());
         getSender().tell(new Result(result), getSelf());
      }else {
         this.unhandled(message);
      }

   }

}
