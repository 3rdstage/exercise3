package thirdstage.sirius.support.truffle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdstage.sirius.support.web3j.ContractMeta;

public interface TruffleArtifactParser{
  
  ContractMeta parseArtifact(String path);
  
  List<ContractMeta> parseArtifacts(String dir);

}
