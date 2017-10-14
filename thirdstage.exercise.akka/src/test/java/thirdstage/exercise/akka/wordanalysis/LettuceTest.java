package thirdstage.exercise.akka.wordanalysis;

import org.testng.annotations.Test;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

public class LettuceTest{

   @Test
   public void testSimplestCommands(){

      int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      RedisClient redisClient = new RedisClient(RedisURI.create("redis://localhost:" + port + "/0"));
      RedisConnection<String, String> conn = redisClient.connect();

      conn.set("greetings", "Hi ~");

      conn.close();
      redisClient.shutdown();
   }


   @Test
   public void testListCommands1(){

      int port = Integer.valueOf(System.getProperty("redis.port", "6379"));

      RedisClient redisClient = new RedisClient(RedisURI.create("redis://localhost:" + port + "/0"));
      RedisConnection<String, String> conn = redisClient.connect();

      conn.rpush("node:1", "{category: 'DSLR', id: 10001, name: 'Canon EOS 600D'}");
      conn.rpush("node:2", "{category: 'Smartphone', id: 10002, name: 'LG G5'}");
      conn.rpush("node:2", "{category: 'Samrtphone', id: 10003, name: '삼성 Galaxy 7'");

      conn.close();
      redisClient.shutdown();
   }
}
