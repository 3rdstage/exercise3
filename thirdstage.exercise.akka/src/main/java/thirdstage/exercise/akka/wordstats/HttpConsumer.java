package thirdstage.exercise.akka.wordstats;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import org.apache.camel.Consume;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import akka.actor.ActorRef;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJob;

public class HttpConsumer extends UntypedConsumerActor{

   private static ObjectMapper jacksonMapper = new ObjectMapper();

   static{
      jacksonMapper.registerModule(new JaxbAnnotationModule())
      .configure(MapperFeature.AUTO_DETECT_FIELDS, false)
      .configure(MapperFeature.AUTO_DETECT_CREATORS, false)
      .configure(MapperFeature.AUTO_DETECT_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_SETTERS, true);
   }

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final String address;

   public String getAddress(){ return this.address; }

   private final int port;

   public int getPort(){ return this.port; }

   private final String uri;

   private final ActorRef service;

   public HttpConsumer(@NotBlank String addr, @Min(1) int port, @Nonnull ActorRef service){
      this.address = addr;
      this.port = port;
      this.uri = "jetty:http://" + addr + ":" + port + "/wordstats";

      this.service = service;
   }


   @Override @Consume
   public String getEndpointUri(){
      return this.uri;
   }

   @Override
   public void onReceive(Object msg) throws Exception{
      if(msg instanceof CamelMessage){
         logger.debug("Received a HTTP request via Camel.");

         CamelMessage msg2 = (CamelMessage) msg;
         String body = msg2.getBodyAs(String.class, getCamelContext());
         StatsJob job = null;
         try{
            job = jacksonMapper.readValue(body, StatsJob.class);
            this.logger.debug("Successfully build the job object parsing the request body.");
            this.service.tell(job, getSelf());
            getSender().tell("OK", getSelf());
         }catch(Exception ex){
            throw new RuntimeException("Fail to parse the request body", ex);
         }

      }else{
         this.unhandled(msg);
      }

   }
}

