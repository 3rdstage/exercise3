package thirdstage.exercise.truffle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.hibernate.validator.constraints.NotEmpty;
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

  protected ContractFacade setQuorumAddress(@NotEmpty String addr) {
    this.quorumAddr = addr;
    return this;
  }

  protected ContractFacade setQuorumPort(@NotEmpty int port) {
    this.quorumPort = port;
    return this;
  }

  public String getQuorumUrl() {
    return String.format("http://%s:%d", this.quorumAddr, this.quorumPort);
  }

}


