package thirdstage.sirius.support.web3j;

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
import org.web3j.abi.datatypes.Address;

/**
 * @author 3rdstage
 * @since 2018-04-10
 */
@ThreadSafe
public class ContractMeta{
  
  private String name;
  
  private Address addr;
  
  public ContractMeta(@NotBlank final String name, @NotBlank final String addr) {
    this.name = name;
    this.addr = new Address(addr);
  }
  
  
  public String getName() { return this.name; }
  
  public Address getAddress() { return this.addr; }
}
