package thirdstage.exercise.truffle.contracts;

import java.util.List;
import java.math.BigInteger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.quorum.Quorum;
import thirdstage.sirius.support.web3j.AbstractQuorumContract;

@Component
public class MetaCoinContract extends AbstractQuorumContract{
  
  @Value("${quorum.contracts.MetaCoin.address}")
  protected String address;

  protected String getAddress() { return this.address; }
  
  @Autowired
  public MetaCoinContract(Quorum defaultQuorum, Quorum fallbackQuorum) {
    super(defaultQuorum, fallbackQuorum);
  }
  
  /**
   * 
   * @see https://web3j.readthedocs.io/en/latest/transactions.html#querying-state
   */
  public BigInteger getBalance(@NotBlank final String addr) {
    final Function func = new Function("getBalance",
        Arrays.asList(new Address(addr)),
        Arrays.asList(new TypeReference<Uint256>() {}));
    final String encodedFunc = FunctionEncoder.encode(func);
    
    final EthCall resp;
    try {
      resp = this.getQuorum().ethCall(
          Transaction.createEthCallTransaction(this.getFrom(), this.getAddress(), encodedFunc), 
          DefaultBlockParameterName.LATEST).sendAsync().get();
      
      if(resp.hasError()) throw new RuntimeException(String.format("%s (code: %d)", resp.getError().getMessage(), resp.getError().getCode()));
      
      List<Type> output = FunctionReturnDecoder.decode(resp.getValue(), func.getOutputParameters());
      return ((Uint256)(output.get(0))).getValue();
    }catch(Exception ex) {
      this.logger.error(ex.getMessage(), ex);
      if(ex instanceof RuntimeException) throw (RuntimeException)ex;
      else throw new RuntimeException(ex);
    }
  }

  /**
   * 
   * @param receiverAddr
   * @param amt
   * 
   * @see https://web3j.readthedocs.io/en/latest/transactions.html#transacting-with-contract 
   */
  public void sendCoin(@NotBlank final String receiverAddr, @Positive int amt) {
    final Function func = new Function("sendCoin",
        Arrays.<Type>asList(new Address(receiverAddr), new Uint256(amt)), 
        Collections.<TypeReference<?>>emptyList());

    final String encodedFunc = FunctionEncoder.encode(func);


  }




}
