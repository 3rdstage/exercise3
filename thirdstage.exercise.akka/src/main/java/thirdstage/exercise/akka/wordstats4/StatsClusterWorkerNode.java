package thirdstage.exercise.akka.wordstats4;

import javax.validation.constraints.Min;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.hibernate.validator.constraints.NotBlank;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;
import thirdstage.exercise.akka.wordstats.StatsService;
import thirdstage.exercise.akka.wordstats.StatsWorker;

public class StatsClusterWorkerNode extends StatsClusterNode{


   private final String masterAddress;

   public String getMasterAddress(){ return this.masterAddress; }

   @Min(1)
   private final int masterBrokerPort;

   public int getMasterBrokerPort(){ return this.masterBrokerPort; }

   public StatsClusterWorkerNode(@Min(1) int nettyPort, @NotBlank String masterAddress,
         @Min(1) int masterBrokerPort, String configSubtree) throws Exception{
      super(nettyPort, configSubtree);

      this.masterAddress = masterAddress;
      this.masterBrokerPort = masterBrokerPort;
   }


   @Override
   public void start(boolean allowsLocalRoutees) throws Exception{

      String mqJmsUrl = "tcp://" + this.getMasterAddress() + ":" + this.getMasterBrokerPort();

      this.config = ConfigFactory.parseString("akka.actor.deployment.default.cluster.allow-local-routees = "
            + (allowsLocalRoutees ? "on" : "off")).withFallback(config);
      this.system = ActorSystem.create(ACTOR_SYSTEM_NAME_DEFAULT,
            ConfigFactory.parseString("akka.remote.netty.tcp.port=" + this.getNettyPort()).withFallback(this.config));

      Camel camel = CamelExtension.get(this.system);
      CamelContext camelCntx = camel.context();
      ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqJmsUrl);
      ActiveMQComponent comp = ActiveMQComponent.activeMQComponent();
      comp.setConnectionFactory(factory);
      camelCntx.addComponent("activemq", comp);

      ActorRef worker = this.system.actorOf(Props.create(StatsWorker.class), "statsWorker");
      ActorRef service = this.system.actorOf(Props.create(StatsService.class), "statsService");

      System.out.println("The master node of Akka cluster has started.\nType return key to exit");
      System.in.read();

      new Thread(){
         @Override
         public void run(){ system.shutdown(); }
      }.start();
   }
}
