package thirdstage.sirius.support.web3j;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.quorum.Quorum;

@Component
public abstract class AbstractQuorumContract{

  @Autowired
  private Quorum defaultQuorum;
  
  @Autowired
  private Quorum fallbackQuorum;
 
  private Quorum quorum;
  
  public AbstractQuorumContract() {
    this.quorum = defaultQuorum;
    
  }  
  
}
