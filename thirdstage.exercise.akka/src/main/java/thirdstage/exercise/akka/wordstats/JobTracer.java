package thirdstage.exercise.akka.wordstats;

import akka.camel.javaapi.UntypedProducerActor;

public class JobTracer extends UntypedProducerActor{

   public JobTracer(){
   }

   @Override
   public String getEndpointUri(){
      return "activemq:queue:JobTrace";
   }

}
