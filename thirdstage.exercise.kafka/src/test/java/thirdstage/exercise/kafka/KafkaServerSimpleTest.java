package thirdstage.exercise.kafka;

import java.util.List;
import java.util.Properties;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import kafka.admin.AdminUtils;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import scala.collection.JavaConversions;

public class KafkaServerSimpleTest{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testStartUp() throws Exception{

      final String zkConnStr = "127.0.0.1:2181";
      final String host = "127.0.0.1";
      final int port = 9090;
      final String id = "1";

      final Properties props = new Properties();
      props.setProperty("host.name", host);
      props.setProperty("port", String.valueOf(port));
      props.setProperty("broker.id", id);
      props.setProperty("log.dir", StringUtils.defaultIfBlank(System.getenv("TEMP"), System.getenv("TMP")) + "/kafka");
      props.setProperty("zookeeper.connect", zkConnStr);

      final KafkaConfig config = new KafkaConfig(props);

      final KafkaServerStartable server = new KafkaServerStartable(config);
      try{
         server.startup();
      }catch(Throwable ex){
         ex.printStackTrace(System.out);
      }

      System.out.println("Server started.");

      Runtime.getRuntime().addShutdownHook(new Thread(){

         @Override
         public void run(){
            server.awaitShutdown();
         }
      });

      final ZkClient zkClient = new ZkClient(zkConnStr, 10 * 1000, 5 * 1000, ZKStringSerializer$.MODULE$);
      final ZkConnection zkConn = new ZkConnection(zkConnStr);
      final ZkUtils zkUtils = new ZkUtils(zkClient, zkConn, false);

      final List<String> topics = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());

      if(topics == null || topics.isEmpty()){
         logger.info("The Kafka broker at {}:{} has no topics", host, port);
      }else{
         logger.info("The Kafka broker at {}:{} has {} topics", host, port, topics.size());
      }

      if(!topics.contains("orders")){
         AdminUtils.createTopic(zkUtils, "orders", 5, 1, new Properties(), kafka.admin.RackAwareMode.Enforced$.MODULE$);
      }


      System.out.println("Press [Enter] key to end this JVM.");
      int cnt;
      while((cnt = System.in.available()) < 1){
         Thread.sleep(500);
      }
      while(cnt-- > 0) System.in.read();

   }
}
