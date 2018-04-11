package thirdstage.sirius.support.truffle;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import com.jayway.jsonpath.JsonPath;
import thirdstage.sirius.support.web3j.ContractMeta;

@ThreadSafe
public class TruffleArtifactParserImpl implements TruffleArtifactParser{

  @Override
  public ContractMeta parseArtifact(String resourceLoc, @NotBlank String networkId) throws Exception{
    final File f = ResourceUtils.getFile(resourceLoc);
    
    JsonPath path1 = JsonPath.compile("$.contractName");
    JsonPath path2 = JsonPath.compile(String.format("$.networks.%s.address", networkId));
    
    String name = path1.read(f);
    String addr = path2.read(f);
    
    return new ContractMeta(name, addr);
  }

  @Override
  public List<ContractMeta> parseArtifacts(String dir, @NotBlank String networkId){
    // TODO Auto-generated method stub
    return null;
  }

}
