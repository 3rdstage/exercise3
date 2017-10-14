package thirdstage.exercise.akka.wordanalysis;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.typesafe.config.Config;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ClusterWorker extends ClusterNodeBase{

   private RedisClient redisClient;

   private final ObjectMapper jacksonMapper = new ObjectMapper();

   public ClusterWorker(@Pattern(regexp="[a-zA-Z0-9]+") String clusterName,
         @Pattern(regexp="[a-zA-Z0-9]+") String applName,
         @Min(1) @Max(0xFFFF) int nettyPort, String configSubtree) throws Exception{
      super(clusterName, applName, nettyPort, configSubtree);

      this.jacksonMapper.registerModule(new JaxbAnnotationModule())
      .configure(MapperFeature.AUTO_DETECT_FIELDS, false)
      .configure(MapperFeature.AUTO_DETECT_CREATORS, false)
      .configure(MapperFeature.AUTO_DETECT_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_SETTERS, true);

   }

   @Override
   protected ActorSystem buildActorSystem(Config config) throws Exception{

      //create redis client
      String redisUri = "redis://" + config.getString("components.redis.address") + ":"
            + config.getString("components.redis.port") + "/0";
      this.redisClient = new RedisClient(RedisURI.create(redisUri));

      ActorSystem system = ActorSystem.create(this.getClusterName(), config);

      String recorderKeyBase = this.getClusterName() + ":" + "words";
      ActorRef recordingService = system.actorOf(Props.create(RecordingService.class,
            recorderKeyBase, this.redisClient), "recorderService");

      String tracerKey = this.getClusterName() + ":trace:node:2";
      ActorRef traceLogService = system.actorOf(Props.create(TraceLogService.class,
            tracerKey, this.redisClient, this.jacksonMapper), "traceLogService");

      ActorRef analysisService = system.actorOf(Props.create(AnalysisService.class,
            recordingService, traceLogService), "analysisService");

      return system;
   }

   @Override
   protected void stopComponents(){
      if(this.redisClient != null){
         this.redisClient.shutdown();
      }
   }

}
