package thirdstage.exercise.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * @param host
   * @param port
   * @return
   *
   * @see https://docs.spring.io/spring-data/redis/docs/2.1.9.RELEASE/api/index.html?org/springframework/data/redis/connection/lettuce/LettuceConnectionFactory.html
   */
  @Bean
  LettuceConnectionFactory redisConnectionFactory(
      @Value("${redis.host}") String host,
      @Value("${redis.port}") int port) {

    LettuceConnectionFactory factory;
    try {
      factory = new LettuceConnectionFactory(host, port);

      logger.info("Redis connection factory created for {}:{}", host, port);
    }catch(Exception ex) {
      logger.error("Fail to create Redis connection factory.", ex);
    }

    return new LettuceConnectionFactory(host, port);
  }

}
