package thirdstage.sirius.support.truffle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdstage.sirius.support.web3j.ContractMeta;

public interface TruffleArtifactParser{
  
  /**
   * @param resourceLoc
   * @return
   * 
   * @see https://docs.spring.io/spring/docs/4.3.x/spring-framework-reference/html/resources.html#resources-resourceloader
   */
  ContractMeta parseArtifact(@NotBlank String resourceLoc, @NotBlank String networkId) throws Exception;
  
  List<ContractMeta> parseArtifacts(String dir, @NotBlank String networkId) throws Exception;

}
