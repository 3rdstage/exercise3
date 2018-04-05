package thirdstage.exercise.truffle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContractFacade{

  @Value("${quorum.address}")
  private String quorumAddr;

  @Value("${quorum.port}")
  private int quorumPort;
  
  
  public ContractFacade() {
    
  }

  public ContractFacade(@NotEmpty String quorumAddr, @Positive @Max(65000) int quorumPort) {
    this.quorumAddr = quorumAddr;
    this.quorumPort = quorumPort;
    
  }

  public String getQuorumUrl() {
    return String.format("http://%s:%d", this.quorumAddr, this.quorumPort);
  }

}


