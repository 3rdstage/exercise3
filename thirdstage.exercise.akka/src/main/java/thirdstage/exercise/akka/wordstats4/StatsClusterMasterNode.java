package thirdstage.exercise.akka.wordstats4;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import thirdstage.exercise.akka.wordstats.HttpConsumer;
import thirdstage.exercise.akka.wordstats.StatsService;
import thirdstage.exercise.akka.wordstats.StatsWorker;

public class StatsClusterMasterNode extends StatsClusterNode{


   public static final int MASTER_NODE_NETTY_PORT_DEFAULT = 2551;

   public static final int HTTP_PORT_DEFAULT = 8080;

   public static final int MQ_CLIENT_CONNECTOR_PORT_DEFAULT = 61606;

   public static final int MQ_JMX_REMOTE_PORT_DEFAULT = 1099;

   public static final int MQ_ADMIN_PORT_DEFAULT = 8090;

   public static final String MQ_BROKER_NAME_DEFAULT = "WordStatsBroker";

   private final int httpPort;

   public int getHttpPort(){ return this.httpPort; }

   private final int mqClientConnectorPort;

   public int getMqClientConnectorPort(){ return this.mqClientConnectorPort; }

   private final int mqJmxRemotePort;

   public int getMqJmxRemotePort(){ return this.mqJmxRemotePort; }

   private final int mqAdminPort;

   public int getMqAdminPort(){ return this.mqAdminPort; }

   public StatsClusterMasterNode(@Min(1) int nettyPort, int httpPort,
         @Min(1) int mqClientConnectorPort, @Min(1) int mqJmxRemotePort, @Min(1) int mqAdminPort, String configSubtree) throws Exception{

      super(nettyPort, configSubtree);

      this.httpPort = httpPort;
      this.mqClientConnectorPort = mqClientConnectorPort;
      this.mqJmxRemotePort = mqJmxRemotePort;
      this.mqAdminPort = mqAdminPort;
   }

   @Override
   public void start(boolean allowsLocalRoutees) throws Exception{

      String mqJmsUrl = this.startActiveMq();

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
      ActorRef httpConsumer = this.system.actorOf(Props.create(HttpConsumer.class,
            this.getAddress().getHostAddress(), httpPort, service), "httpConsumer");

      Future<ActorRef> activationFuture = camel.activationFutureFor(httpConsumer,
            new Timeout(Duration.create(10, TimeUnit.SECONDS)), this.system.dispatcher());

      System.out.println("The master node of Akka cluster has started.\nType return key to exit");
      System.in.read();

      new Thread(){
         @Override
         public void run(){ system.shutdown(); }
      }.start();

   }

   private String startActiveMq() throws Exception{

      System.getProperties().put("activemq.openwire.address", this.getAddress().getHostAddress());
      System.getProperties().put("activemq.openwire.port", String.valueOf(this.getMqClientConnectorPort()));
      String mqJmsUrl = "tcp://" + System.getProperty("activemq.openwire.address") +
            ":" + System.getProperty("activemq.openwire.port");

      final BrokerService broker = BrokerFactory.createBroker(
            new URI("xbean:thirdstage/exercise/akka/wordstats/activemq.xml"));

      //setup and load Jetty with ActiveMQ web console
      System.getProperties().put("webconsole.type", "properties");
      System.getProperties().put("webconsole.jms.url", mqJmsUrl);
      System.getProperties().put("webconsole.jmx.url",
            "service:jmx:rmi:///jndi/rmi://" + this.getAddress().getHostAddress() + ":" + this.getMqJmxRemotePort() + "/jmxrmi");
      final Server jetty = new Server(new InetSocketAddress(this.getAddress(), this.mqAdminPort));
      jetty.addBean(new MBeanContainer(ManagementFactory.getPlatformMBeanServer()));
      Configuration.ClassList.serverDefault(jetty).addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration"
            , "org.eclipse.jetty.annotations.AnnotationConfiguration");
      final String webAppPath = System.getenv("TEMP") + "/activemq-web-console-modified.war";
      final WebAppContext webApp = new WebAppContext(webAppPath, "/admin");
      webApp.setExtractWAR(true);
      webApp.setLogUrlOnStart(true);
      webApp.setAttribute(
            "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
            ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );
      jetty.setHandler(webApp);
      jetty.setStopAtShutdown(true);
      jetty.start();
      //jetty.join(); //not mandatory but...

      return mqJmsUrl;

   }


}
