package thirdstage.sirius.support.web3j;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.web3j.quorum.Quorum;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class QuorumNodeInspectorTest{
  
  @Autowired
  private Quorum defaultQuorum;
  
  private QuorumNodeInspector inspector;
  
  @BeforeAll
  public void beforeAll(){
    this.inspector = new QuorumNodeInspector(this.defaultQuorum);
  }

  @Test
  public void testQuorumNotNull(){
    Assertions.assertNotNull(defaultQuorum);
  }

  @Test
  public void testGetBlockNumber() {
    BigInteger num = this.inspector.getBlockNumber();
    
    Assertions.assertNotNull(num);
    Assertions.assertTrue(num.longValue() > 0);
    
  }
  
  @Test
  public void testGetCoinbase() {
    String addr = this.inspector.getCoinbase();
    
    Assertions.assertTrue(StringUtils.isNotBlank(addr));
    
  }
  
}
