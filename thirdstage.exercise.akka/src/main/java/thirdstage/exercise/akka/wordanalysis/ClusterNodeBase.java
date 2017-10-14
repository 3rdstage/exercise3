package thirdstage.exercise.akka.wordanalysis;

import java.net.InetAddress;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.LoggerFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorSystem;

public abstract class ClusterNodeBase implements ClusterNode{

   protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final String clusterName;

   public String getClusterName(){ return this.clusterName; }

   private final String applName;

   public String getApplicationName(){ return this.applName; }

   private final InetAddress address;

   public InetAddress getAddress(){ return this.address; }

   private final int nettyPort;

   public int getNettyPort(){ return this.nettyPort; }

   private final String configSubtree;

   public String getConfigSubtree(){ return this.configSubtree; }

   private String pid;

   public String getPID(){ return this.pid; }

   public ClusterNodeBase setPID(String pid){
      this.pid = pid;
      return this;
   }

   //@TODO Try to find to expose the config instance via platform JMX server
   private Config config;

   protected Config getConfig(){ return this.config; }

   //@TODO Try to find to expose the actor system instance via platform JMX server
   private ActorSystem system;

   //@TODO Why set to public ?
   public ActorSystem getActorSystem(){ return this.system; }

   public ClusterNodeBase(@NotBlank String clusterName, @NotBlank String applName,
         @Min(1) @Max(0xFFFF) int nettyPort, @Nullable String configSubtree) throws Exception{
      this.clusterName = clusterName;
      this.applName = applName;
      this.nettyPort = nettyPort;
      this.configSubtree = configSubtree;
      this.address = InetAddress.getLocalHost(); //@Important Do NOT use local loopback

      this.config = ConfigFactory.load();
      if(!StringUtils.isBlank(configSubtree)){
         this.config = this.config.getConfig(this.getConfigSubtree()).withFallback(this.config);
      }
      this.config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(this.config);
      this.config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + this.address.getHostAddress()).withFallback(this.config);
      this.config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + nettyPort).withFallback(this.config);
   }

   @Override
   public final void start() throws Exception{
      this.start(false);
   }

   @Override
   public final void start(boolean allowsLocalRoutees) throws Exception{
      this.config = ConfigFactory.parseString("akka.actor.deployment.default.cluster.allow-local-routees = "
            + (allowsLocalRoutees ? "on" : "off")).withFallback(config);

      try{
         this.system = this.buildActorSystem(this.config);
         this.logger.info("Succeed to build actor system at {}:{}.",
               this.address.getHostAddress(), this.getNettyPort());

      }catch(Exception ex){
         this.logger.error("Fail to build actor system at {}:{}.",
               this.address.getHostAddress(), this.getNettyPort(), ex);
         throw new IllegalStateException("Fail to build actor system.", ex);
      }
   }

   protected abstract ActorSystem buildActorSystem(@Nonnull Config config) throws Exception;

   @Override
   public final void stop() throws Exception{
      this.stopComponents();

      if(this.system != null){
         try{
            this.system.shutdown();
            this.logger.info("Finished to shutdown the actor system");
         }catch(Exception ex){
            this.logger.error("Fail to shutdown the actor system", ex);
         }
      }
   }

   protected abstract void stopComponents();

}
