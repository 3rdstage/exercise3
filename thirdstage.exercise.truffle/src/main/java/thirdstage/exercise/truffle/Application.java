package thirdstage.exercise.truffle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;

@SpringBootApplication
public class Application{
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Bean
  public Quorum defaultQuorum(@Value("${quorum.address}") @NotEmpty String quorumAddr, 
      @Value("${quorum.port}") @Positive int quorumPort) {
    return buildQuorumClient(quorumAddr, quorumPort);
  }
  
  @Bean Quorum fallbackQuorum(@Value("${quorum-fallback.address}") @NotEmpty String quorumAddr, 
      @Value("${quorum-fallback.port}") @Positive int quorumPort) {
    return buildQuorumClient(quorumAddr, quorumPort);
  }
  
  private Quorum buildQuorumClient(@NotEmpty String addr, @Positive int port) {
    Quorum client = Quorum.build(new HttpService(String.format("http://%s:%d", addr, port)));
    
    logger.info("Quorum client is created for http://{}:{}", addr, port);
    
    return client;
  }
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
  
  
}

