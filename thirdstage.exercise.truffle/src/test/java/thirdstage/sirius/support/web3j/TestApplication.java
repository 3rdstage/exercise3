package thirdstage.sirius.support.web3j;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import thirdstage.exercise.truffle.Application;

@SpringBootApplication
public class TestApplication{

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
