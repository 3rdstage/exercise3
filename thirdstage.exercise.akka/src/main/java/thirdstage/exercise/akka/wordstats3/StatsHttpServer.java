package thirdstage.exercise.akka.wordstats3;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.model.MediaType;
import akka.http.javadsl.model.RequestEntity;
import akka.http.javadsl.server.Handler;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.RequestContext;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.RouteResult;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJob;
import thirdstage.exercise.akka.wordstats.StatsService;
import thirdstage.exercise.akka.wordstats.StatsWorker;


public class StatsHttpServer{

   public static final int NETTY_PORT_DEFAULT = 2551;

   public static final int HTTP_PORT_DEFAULT = 8080;

   public static final String ACTOR_SYSTEM_NAME_DEFAULT = "WordStats";

   public static final String APPL_NAME_DEFAULT = ACTOR_SYSTEM_NAME_DEFAULT.toLowerCase();

   private static String pid;

   public static void main(String[] args) throws Exception{
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);


      if(args.length > 2 || args.length < 1){
         throw new IllegalArgumentException("One or two argument(s) is(are) expected.");
      }else if(args.length == 1){
         (new StatsHttpServer(Integer.valueOf(args[0]), -1, APPL_NAME_DEFAULT)).start();
      }else if(args.length == 2){
         (new StatsHttpServer(Integer.valueOf(args[0]), Integer.valueOf(args[1]), APPL_NAME_DEFAULT)).start();
      }
   }

   private final int[] nettyPorts;

   public int[] getNettyPorts(){ return this.nettyPorts; }

   private final int httpPort;

   public int getHttpPort(){ return this.httpPort; }

   private final String configSubtree;

   public String getConfigSubtree(){ return this.configSubtree; }

   private Config config;

   private ActorSystem[] systems;

   public StatsHttpServer(@Min(1) int nettyPort, int httpPort, String configSubtree){
      int[] ports = new int[1];
      ports[0] = nettyPort;

      this.nettyPorts = ports;
      this.httpPort = httpPort;
      this.configSubtree = configSubtree;
   }

   public StatsHttpServer(int[] nettyPorts, int httpPort, String configSubtree){
      this.nettyPorts = nettyPorts;
      this.httpPort = httpPort;
      this.configSubtree = configSubtree;
   }

   public void start() throws Exception{

      this.config = ConfigFactory.load();
      this.config = this.config.getConfig(this.getConfigSubtree()).withFallback(this.config);
      this.config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);

      this.systems = new ActorSystem[this.nettyPorts.length];
      for(int i = 0, n = this.nettyPorts.length; i < n; i++){
         systems[i] = ActorSystem.create(ACTOR_SYSTEM_NAME_DEFAULT,
               ConfigFactory.parseString("akka.remote.netty.tcp.port=" + this.getNettyPorts()[i]).withFallback(this.config));
         ActorRef worker = systems[i].actorOf(Props.create(StatsWorker.class), "statsWorker");
         ActorRef service = systems[i].actorOf(Props.create(StatsService.class), "statsService");

         if(i == 0 && this.httpPort > 1) new StatsHttpApp(systems[i]).bindRoute("localhost", this.httpPort, systems[i]);
      }

      System.out.println("Type return to exit");
      System.in.read();

      for(final ActorSystem system : this.systems){
         new Thread(){
            @Override
            public void run(){ system.shutdown(); }
         }.start();
      }
   }

   private static class StatsHttpApp extends HttpApp{

      private static int count = 1;

      private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

      private final ActorSystem system;

      public StatsHttpApp(@Nonnull ActorSystem system){
         this.system  = system;
      }

      @Override
      public Route createRoute(){

         return route(
               get(
                     path("test").route(handleWith(testHandler)),
                     path("jsontest").route(handleWith(jsonTestHandler)),
                     path("wordstats").route(handleWith(wordStatsHandler))

                     ),
               put(
                     path("jsontest").route(handleWith(jsonTestHandler))
                     )

               );
      }

      private final Handler wordStatsHandler = new Handler(){
         private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

         @Override
         public RouteResult apply(RequestContext cntx){
            RequestEntity entity = cntx.request().entity();
            MediaType mediaType = entity.getContentType().mediaType();

            this.logger.debug("Received HTTP reqeust. - Method: {}, URI: {}, Media Type: {}",
                  cntx.request().method().value(), cntx.request().getUri(), mediaType.toString());

            return cntx.complete(String.format("Received HTTP reqeust. - Method: %s, URI: %s, Media Type: %s",
                  cntx.request().method().value(), cntx.request().getUri(), mediaType.toString()));

         }
      };

      private final Handler jsonTestHandler = new Handler(){
         private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

         @Override
         public RouteResult apply(RequestContext cntx){
            RequestEntity entity = cntx.request().entity();
            MediaType mediaType = entity.getContentType().mediaType();
            Source<ByteString, Object> data = entity.getDataBytes();

            this.logger.debug("Received HTTP reqeust. - Method: {}, URI: {}, Media Type: {}",
                  cntx.request().method().value(), cntx.request().getUri(), mediaType.toString());

            return cntx.complete(String.format("Received HTTP reqeust. - Method: %s, URI: %s, Media Type: %s",
                  cntx.request().method().value(), cntx.request().getUri(), mediaType.toString()));

         }
      };

      private final Handler testHandler = new Handler(){
         private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

         @Override
         public RouteResult apply(RequestContext cntx){
            this.logger.debug("Received the {} request to {}", cntx.request().method().value(), cntx.request().getUri());

            ActorSelection service = system.actorSelection("/user/statsService");
            service.tell(new StatsJob(String.valueOf(count++), "You're just good to be true"), service.anchor());


            return cntx.complete(String.format("This was a %s  request to %s",
                  cntx.request().method().value(), cntx.request().getUri()));
         }
      };


   }


}
