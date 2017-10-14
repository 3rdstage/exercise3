package thirdstage.exercise.akka.wordanalysis;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.typesafe.config.Config;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;
import akka.util.Timeout;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.KeyNodeMap;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.KeyNodeMapHoconProvider;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.KeyNodeMapProvider;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.SimpleKeyNodeMap;

public class ClusterMaster2 extends ClusterNodeBase{

   public static final int MASTER_NODE_NETTY_PORT_DEFAULT = 2551;

   public static final int HTTP_PORT_DEFAULT = 8080;

   private final int httpPort;

   public int getHttpPort(){ return this.httpPort; }

   private String baseUrl;

   public String getBaseUrl(){ return this.baseUrl; }

   private RedisServer redis;

   private RedisClient redisClient;

   private final ObjectMapper jacksonMapper = new ObjectMapper();

   private KeyNodeMap<String> keyNodeMap = new SimpleKeyNodeMap<String>();

   public KeyNodeMap<String> getKeyNodeMap(){ return this.keyNodeMap; }

   public void setKeyNodeMap(@Nullable KeyNodeMap<String> keyNodeMap){
      if(keyNodeMap == null){
         this.keyNodeMap = new SimpleKeyNodeMap<String>();
      }else{
         this.keyNodeMap = keyNodeMap;
      }
   }

   public ClusterMaster2(@Pattern(regexp="[a-zA-Z0-9]+") String clusterName,
         @Pattern(regexp="[a-zA-Z0-9]+") String applName,
         @Min(1) @Max(0xFFFF) int nettyPort, @Min(1) @Max(0xFFFF) int httpPort,
         String configSubtree) throws Exception{
      super(clusterName, applName, nettyPort, configSubtree);
      this.httpPort = httpPort;

      //validate the master
      String addr = null;
      int port = -1;
      try{
         addr = this.getConfig().getString("nodes.master.address");
         port = this.getConfig().getInt("nodes.master.port");
      }catch(Exception ex){
         throw new IllegalStateException("The address or port for the master is not defined at configuration");
      }

      if(!StringUtils.equals(addr, this.getAddress().getHostAddress())
            || this.getNettyPort() != port){
         throw new IllegalStateException("The address or port for this master is not same with thosed defined in configuration.");
      }

      this.jacksonMapper.registerModule(new JaxbAnnotationModule())
      .configure(MapperFeature.AUTO_DETECT_FIELDS, false)
      .configure(MapperFeature.AUTO_DETECT_CREATORS, false)
      .configure(MapperFeature.AUTO_DETECT_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_SETTERS, true);

   }

   @Override
   protected ActorSystem buildActorSystem(Config config) throws Exception{
      //key node map
      KeyNodeMapProvider<String> provider = new KeyNodeMapHoconProvider(config);
      this.setKeyNodeMap(provider.getKeyNodeMap());

      //start redis
      final int redisPort = (config.hasPath("components.redis.port")) ? config.getInt("components.redis.port") : RedisURI.DEFAULT_REDIS_PORT;
      final String redisLogLevel = config.hasPath("components.redis.log-level") ? config.getString("components.redis.log-level") : "verbose";
      String logBase = System.getenv("LOG_BASE");
      if(StringUtils.isBlank(logBase)) logBase = System.getenv("TEMP");
      final String redisLogFile = config.hasPath("components.redis.log-file") ? config.getString("components.redis.log-file") : logBase + "\\redis.log";
      final String redisPidFile = config.hasPath("components.redis.pid-file") ? config.getString("components.redis.pid-file") : logBase + "\\redis.pid";

      try{
         this.redis = RedisServer.builder()
               .redisExecProvider(RedisExecProvider.defaultProvider())
               .port(redisPort)
               .setting("loglevel " + redisLogLevel)
               .setting("logfile " + redisLogFile)
               .setting("pidfile " + redisPidFile)
               .build();
      }catch(Exception ex){
         this.logger.error("Fail to build redis server.", ex);
         throw new IllegalStateException("Fail to build redis server.", ex);
      }
      new Thread(){
         @Override
         public void run(){
            try{
               redis.start();
               logger.info("Started redis server on {} port", redisPort);
            }catch(Exception ex){
               logger.error("Fail to start redis server.", ex);
               //@TODO Use future to stop the actor system at this point.
            }
         }
      }.start();

      //create redis client
      String redisUri = "redis://" + this.getAddress().getHostAddress() + ":" + redisPort + "/0";
      this.redisClient = new RedisClient(RedisURI.create(redisUri));

      ActorSystem system = ActorSystem.create(this.getClusterName(), config);
      Camel camel = CamelExtension.get(system);

      this.baseUrl = "http://" + this.getAddress().getHostAddress() + ":"
            + this.getHttpPort() + "/" + this.getApplicationName();
      String uri = "jetty:" + this.baseUrl;

      String recorderKeyBase = this.getClusterName() + ":" + "words";
      ActorRef recordingService = system.actorOf(Props.create(RecordingService.class,
            recorderKeyBase, this.redisClient), "recorderService");

      String tracerKey = this.getClusterName() + ":trace:node:1";
      ActorRef traceLogService = system.actorOf(Props.create(TraceLogService.class,
            tracerKey, this.redisClient, this.jacksonMapper), "traceLogService");

      ActorRef analysisService = system.actorOf(Props.create(AnalysisService.class,
            recordingService, traceLogService), "analysisService");

      ActorRef httpClerk = system.actorOf(Props.create(WebService2.class, uri, keyNodeMap, "analysisService"), "httpClerk");

      Future<ActorRef> activationFuture = camel.activationFutureFor(httpClerk,
            new Timeout(Duration.create(10, TimeUnit.SECONDS)), system.dispatcher());

      return system;
   }

   @Override
   protected void stopComponents(){

      if(this.redisClient != null){ this.redisClient.shutdown(); }

      try{
         new Thread(){
            @Override public void run(){
               if(redis != null){ redis.stop(); }
            }
         }.start();
      }catch(Exception ex){
         logger.error("Fail to stop redis", ex);
      }
   }

}
