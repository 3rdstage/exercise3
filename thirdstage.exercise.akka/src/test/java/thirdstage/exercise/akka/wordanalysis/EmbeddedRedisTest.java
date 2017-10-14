package thirdstage.exercise.akka.wordanalysis;

import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import redis.embedded.RedisServer;

@Test(singleThreaded=true)
public class EmbeddedRedisTest{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


   @Test
   public void testSimplestRun() throws Exception{

      int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      final RedisServer server = new RedisServer(port);
      server.start();

      Runtime.getRuntime().addShutdownHook(new Thread(){
         @Override
         public void run(){
            server.stop();
         }
      });

      System.out.println("A redis server has started on " + port + " port.\nType return key to stop redis and end the process.");
      System.in.read();

   }


   @Test
   public void testStop() throws Exception{

      int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      final RedisServer server = RedisServer.builder().port(port).build();
      server.start();

      System.out.println("A redis server has started on " + port + " port.\nType return key to stop redis and end the process.");
      System.in.read();

      server.stop();

      System.out.println("Redis has stopped. Check the process at the task manager");
      System.in.read();
   }


   @Test
   public void testAsyncStop() throws Exception{

      int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      final RedisServer server = RedisServer.builder().port(port).build();
      server.start();

      System.out.println("A redis server has started on " + port + " port.\nType return key to stop redis and end the process.");
      int cnt = 0;
      while((cnt = System.in.available()) < 1){
         Thread.sleep(500);
      }
      while(cnt-- > 0) System.in.read();

      new Thread(){
         @Override
         public void run(){
            server.stop();
            System.out.println("Redis has stopped. Check the process at the task manager");
         }
      }.start();


      //System.in.read();
   }

   @Test
   public void testAsyncStop2() throws Exception{

      final int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      final RedisServer server = RedisServer.builder()
            .port(port)
            .setting("loglevel verbose")  //one among 'debug', 'verbose', 'notice', or 'warning'
            .setting("logfile C:\\temp\\redis.log")
            .build();

      server.start(); //@TODO Why hang-up here?

      logger.info("Redis server has started on " + port + " port.");
      System.out.println("Type return key to stop redis and end the process.");
      int cnt = 0;
      while((cnt = System.in.available()) < 1){
         Thread.sleep(500);
      }
      while(cnt-- > 0) System.in.read();

      new Thread(){
         @Override
         public void run(){
            logger.info("Trying to stop redis server.");
            server.stop();
            logger.info("Redis has stopped. Check the process at the task manager.");
         }
      }.start();

      System.out.println("Type return key to stop redis and end the process.");
      System.in.read();
   }

}
