package thirdstage.sirius.support.truffle;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdstage.sirius.support.web3j.ContractMeta;

class TruffleArtifactParserImplTest{
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private TruffleArtifactParser parser = new TruffleArtifactParserImpl();

  
  @Test
  void testParseArtifact() throws Exception{
    String res = "classpath:thirdstage/sirius/support/truffle/MetaCoin.json";
    
    ContractMeta meta = parser.parseArtifact(res, "1991");

    this.logger.debug("Contract : name - {}, address - {}", meta.getName(), meta.getAddress());
    
    Assertions.assertSame("MetaCoin", meta.getName());
    Assertions.assertSame("0x46b7e1b682dde9b02f7e2800a7287c0261b78b62", meta.getAddress());
    
  }

}
