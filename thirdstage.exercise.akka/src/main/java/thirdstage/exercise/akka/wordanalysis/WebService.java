package thirdstage.exercise.akka.wordanalysis;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import org.apache.camel.Consume;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.typesafe.config.Config;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.Router;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.MappedRouterConfig;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.RoutingMap;

/**
 * @author Sangmoon Oh
 * @see WebService2
 */
@Deprecated
public class WebService extends UntypedConsumerActor{

   public static final int TIMEOUT_DEFAULT = 3000;

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

   private final Router router;

   private final FiniteDuration timeout;

   public WebService(@NotBlank String uri, @Nonnull RoutingMap<String> routingMap){
      this(uri, routingMap, TIMEOUT_DEFAULT);
   }

   public WebService(@NotBlank String uri, @Nonnull RoutingMap<String> routingMap, @Min(TIMEOUT_MIN) int timeout){

      this.uri = uri;
      Config config = this.getContext().system().settings().config();
      this.router = (new MappedRouterConfig<String>(routingMap)).createRouter(this.getContext().system());

      this.timeout = Duration.create(timeout, TimeUnit.MILLISECONDS);
   }

   @Override @Consume
   public String getEndpointUri(){
      return this.uri;
   }

   @Override
   public FiniteDuration replyTimeout(){
      return this.timeout;
   }

   /* (non-Javadoc)
    * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
    */
   @Override
   public void onReceive(Object msg) throws Exception{
      if(msg instanceof CamelMessage){
         logger.debug("Received a HTTP request via Camel.");

         CamelMessage msg2 = (CamelMessage) msg;
         String body = msg2.getBodyAs(String.class, getCamelContext());
         Sentence setence = null;
         try{
            setence = jacksonMapper.readValue(body, Sentence.class);
            this.logger.debug("Successfully has parsed the request body.");

            this.router.route(setence, this.getSelf());

            this.logger.debug("Successfully routed the request");
            this.getSender().tell("Okay", getSelf());
         }catch(Exception ex){
            throw new RuntimeException("Fail to parse the request body", ex);
         }

      }else{
         this.unhandled(msg);
      }

   }

}
