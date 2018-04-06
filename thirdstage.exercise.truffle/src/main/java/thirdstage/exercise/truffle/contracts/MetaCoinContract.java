package thirdstage.exercise.truffle.contracts;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import thirdstage.sirius.support.web3j.AbstractQuorumContract;


public class MetaCoinContract extends AbstractQuorumContract{
  
  // Reference
  //  - https://web3j.readthedocs.io/en/latest/transactions.html#transacting-with-contract
  
  
  public void sendCoin(@NotBlank String receiverAddr, @Positive int amt) {
    final Function func = new Function("sendCoin",
        Arrays.<Type>asList(new Address(receiverAddr), new Uint256(amt)), 
        Collections.<TypeReference<?>>emptyList());
    
    String encodedFunc = FunctionEncoder.encode(func);
    
    
    
    
    
  }

}
