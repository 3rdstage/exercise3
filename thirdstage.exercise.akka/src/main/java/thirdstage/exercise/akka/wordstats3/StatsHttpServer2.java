package thirdstage.exercise.akka.wordstats3;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.MDC;
import com.typesafe.config.Config;
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


/**
 * Do NOT use local loopback (127.0.0.1)
 *
 * @author Sangmoon Oh
 *
 */
public class StatsHttpServer2{

   public static final int MASTER_NODE_NETTY_PORT_DEFAULT = 2551;

   public static final int HTTP_PORT_DEFAULT = 8080;

   public static final int MQ_CLIENT_CONNECTOR_PORT_DEFAULT = 61606;

   public static final int MQ_JMX_REMOTE_PORT_DEFAULT = 1099;

   public static final int MQ_ADMIN_PORT_DEFAULT = 8090;

   public static final String ACTOR_SYSTEM_NAME_DEFAULT = "WordStats";

   public static final String MESSAGE_BROKER_NAME_DEFAULT = "WordStatsBroker";

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

   private final int mqClientConnectorPort;

   public int getMqClientConnectorPort(){ return this.mqClientConnectorPort; }

   private final int mqJmxRemotePort;

   public int getMqJmxRemotePort(){ return this.mqJmxRemotePort; }

   private final int mqAdminPort;

   public int getMqAdminPort(){ return this.mqAdminPort; }

   private final String configSubtree;

   public String getConfigSubtree(){ return this.configSubtree; }

   private Config config;

   private ActorSystem[] systems;

   public StatsHttpServer2(int[] nettyPorts, int httpPort, String configSubtree){
      this.nettyPorts = nettyPorts;
      this.httpPort = httpPort;
      this.mqClientConnectorPort = MQ_CLIENT_CONNECTOR_PORT_DEFAULT;
      this.mqJmxRemotePort = MQ_JMX_REMOTE_PORT_DEFAULT;
      this.mqAdminPort = MQ_ADMIN_PORT_DEFAULT;
      this.configSubtree = configSubtree;
   }

   public StatsHttpServer2(@Min(1) int nettyPort, int httpPort, String configSubtree){
      int[] ports = new int[1];
      ports[0] = nettyPort;

      this.nettyPorts = ports;
      this.httpPort = httpPort;
      this.mqClientConnectorPort = MQ_CLIENT_CONNECTOR_PORT_DEFAULT;
      this.mqJmxRemotePort = MQ_JMX_REMOTE_PORT_DEFAULT;
      this.mqAdminPort = MQ_ADMIN_PORT_DEFAULT;
      this.configSubtree = configSubtree;
   }

   public StatsHttpServer2(@Min(1) int nettyPort, int httpPort, @Min(1) int mqAdminPort, String configSubtree){
      int[] ports = new int[1];
      ports[0] = nettyPort;

      this.nettyPorts = ports;
      this.httpPort = httpPort;
      this.mqClientConnectorPort = MQ_CLIENT_CONNECTOR_PORT_DEFAULT;
      this.mqJmxRemotePort = MQ_JMX_REMOTE_PORT_DEFAULT;
      this.mqAdminPort = mqAdminPort;
      this.configSubtree = configSubtree;
   }

   public StatsHttpServer2(@Min(1) int nettyPort, int httpPort,
         @Min(1) int mqClientConnectorPort, @Min(1) int mqJmxRemotePort, @Min(1) int mqAdminPort, String configSubtree){

      int[] ports = new int[1];
      ports[0] = nettyPort;

      this.nettyPorts = ports;
      this.httpPort = httpPort;
      this.mqClientConnectorPort = mqClientConnectorPort;
      this.mqJmxRemotePort = mqJmxRemotePort;
      this.mqAdminPort = mqAdminPort;
      this.configSubtree = configSubtree;

   }
   public void start() throws Exception{
      this.start(false);
   }

   public void start(boolean allowsLocalRoutees) throws Exception{

      final InetAddress addr = InetAddress.getLocalHost(); //@Important Do NOT use local loopback

      //setup and load Akka cluster
      this.config = ConfigFactory.load();
      this.config = this.config.getConfig(this.getConfigSubtree()).withFallback(this.config);
      this.config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);
      this.config = ConfigFactory.parseString("akka.actor.deployment.default.cluster.allow-local-routees = "
            + (allowsLocalRoutees ? "on" : "off")).withFallback(config);

      String masterAddr = this.config.getString("node.master.address");
      int masterPort = this.config.getInt("node.master.port");

      this.systems = new ActorSystem[this.nettyPorts.length];
      for(int i = 0, n = this.nettyPorts.length; i < n; i++){
         systems[i] = ActorSystem.create(ACTOR_SYSTEM_NAME_DEFAULT,
               ConfigFactory.parseString("akka.remote.netty.tcp.port=" + this.getNettyPorts()[i]).withFallback(this.config));

         if(StringUtils.equals(addr.getHostAddress(), masterAddr) && nettyPorts[i] == masterPort){
            String mqJmsUrl = this.startActiveMq();
            Camel camel = CamelExtension.get(systems[i]);
            CamelContext camelCntx = camel.context();
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqJmsUrl);
            ActiveMQComponent comp = ActiveMQComponent.activeMQComponent();
            comp.setConnectionFactory(factory);
            camelCntx.addComponent("activemq", comp);

            ActorRef worker = systems[i].actorOf(Props.create(StatsWorker.class), "statsWorker");
            ActorRef service = systems[i].actorOf(Props.create(StatsService.class), "statsService");
            ActorRef httpConsumer = systems[i].actorOf(Props.create(HttpConsumer.class, addr.getHostAddress(), httpPort, service), "httpConsumer");

            Future<ActorRef> activationFuture = camel.activationFutureFor(httpConsumer,
                  new Timeout(Duration.create(10, TimeUnit.SECONDS)), systems[i].dispatcher());
         }else{
            ActorRef worker = systems[i].actorOf(Props.create(StatsWorker.class), "statsWorker");
            ActorRef service = systems[i].actorOf(Props.create(StatsService.class), "statsService");
         }

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

   private String startActiveMq() throws Exception{
      final InetAddress addr = InetAddress.getLocalHost();

      System.getProperties().put("activemq.openwire.address", addr.getHostAddress());
      System.getProperties().put("activemq.openwire.port", String.valueOf(this.getMqClientConnectorPort()));
      String mqJmsUrl = "tcp://" + System.getProperty("activemq.openwire.address") +
            ":" + System.getProperty("activemq.openwire.port");

      final BrokerService broker = BrokerFactory.createBroker(
            new URI("xbean:thirdstage/exercise/akka/wordstats/activemq.xml"));

      //setup and load Jetty with ActiveMQ web console
      System.getProperties().put("webconsole.type", "properties");
      System.getProperties().put("webconsole.jms.url", mqJmsUrl);
      System.getProperties().put("webconsole.jmx.url",
            "service:jmx:rmi:///jndi/rmi://" + addr.getHostAddress() + ":" + this.getMqJmxRemotePort() + "/jmxrmi");
      final Server jetty = new Server(new InetSocketAddress(addr, this.mqAdminPort));
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
