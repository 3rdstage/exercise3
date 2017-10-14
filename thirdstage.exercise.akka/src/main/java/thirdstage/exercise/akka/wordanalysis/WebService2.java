package thirdstage.exercise.akka.wordanalysis;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import org.apache.camel.Consume;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.KeyNodeMap;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.MappedRouterActor;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.PortBaseNodeIdResolver;

public class WebService2 extends UntypedConsumerActor{

   public static final int TIMEOUT_DEFAULT = 7000;

   public static final int TIMEOUT_MIN = 500;

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

   private final String uri;

   private final ActorRef routerActor;

   private final FiniteDuration timeout;

   /**
    * @param uri
    * @param routerActor
    * @param timeout in milli-seconds
    */
   public WebService2(@NotBlank String uri, @Nonnull KeyNodeMap<String> keyNodeMap,
         @NotBlank String servicePath, @Min(TIMEOUT_MIN) int timeout){
      this.uri = uri;
      this.routerActor = this.getContext().system().actorOf(
            Props.create(MappedRouterActor.class, keyNodeMap, servicePath, new PortBaseNodeIdResolver()),
            "routerActor");
      this.timeout = Duration.create(timeout, TimeUnit.MILLISECONDS);
   }

   public WebService2(@NotBlank String uri, @Nonnull KeyNodeMap<String> keyNodeMap,
         @NotBlank String servicePath){
      this(uri, keyNodeMap, servicePath, TIMEOUT_DEFAULT);
   }

   @Override @Consume
   public String getEndpointUri(){
      return this.uri;
   }

   @Override
   public FiniteDuration replyTimeout(){
      return this.timeout;
   }

   @Override
   public void onReceive(Object msg) throws Exception{
      if(msg instanceof CamelMessage){
         logger.debug("Received a request via Camel");

         CamelMessage msg2 = (CamelMessage)msg;
         String body = msg2.getBodyAs(String.class, getCamelContext());
         Sentence sentence = null;
         try{
            sentence = jacksonMapper.readValue(body, Sentence.class);
            this.logger.debug("Successfully parsed the request body.");

            this.routerActor.tell(sentence, this.getSelf());
            this.logger.debug("Routed the request.");
            this.getSender().tell("Okay", getSelf());
         }catch(Exception ex){
            throw new RuntimeException("Fail to parse or route the request.", ex);
         }
      }else{
         this.unhandled(msg);
      }

   }

}
