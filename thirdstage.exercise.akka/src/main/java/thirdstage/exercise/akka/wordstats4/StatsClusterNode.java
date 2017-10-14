package thirdstage.exercise.akka.wordstats4;

import java.net.InetAddress;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorSystem;

/**
 * @author Sangmoon Oh
 * @since 2016-05-12
 */
public abstract class StatsClusterNode{

   public static final String ACTOR_SYSTEM_NAME_DEFAULT = "WordStats";

   public static final String APPL_NAME_DEFAULT = ACTOR_SYSTEM_NAME_DEFAULT.toLowerCase();

   private final InetAddress address;

   public InetAddress getAddress(){ return this.address; }

   private final int nettyPort;

   public int getNettyPort(){ return this.nettyPort; }

   private final String configSubtree;

   public String getConfigSubtree(){ return this.configSubtree; }

   private String pid;

   public String getPID(){ return this.pid; }

   public void setPID(String pid){ this.pid = pid; }

   protected Config config;

   protected ActorSystem system;

   public StatsClusterNode(int nettyPort, String configSubtree) throws Exception{
      this.nettyPort = nettyPort;
      this.configSubtree = configSubtree;
      this.address = InetAddress.getLocalHost(); //@Important Do NOT use local loopback

      this.config = ConfigFactory.load();
      this.config = this.config.getConfig(this.getConfigSubtree()).withFallback(this.config);
      this.config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(this.config);
   }

   public void start() throws Exception{
      this.start(false);
   }

   public abstract void start(boolean allowsLocalRoutees) throws Exception;
}
