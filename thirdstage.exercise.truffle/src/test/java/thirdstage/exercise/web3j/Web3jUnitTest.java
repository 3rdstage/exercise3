package thirdstage.exercise.web3j;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
  

/**
 * @author 3rdstage
 *
 * @see http://docs.web3j.io/latest/web3j_unit/
 * @see https://github.com/web3j/web3j-unit
 * 
 */
//@Disabled
@EVMTest(type=NodeType.EMBEDDED, 
  genesis = "file:src/test/resources/thirdstage/exercise/web3j/genesis.json")
public class Web3jUnitTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Test
  public void testBase(final Web3j web3j, final TransactionManager txMgr, 
      final ContractGasProvider gasMgr) throws Exception{
    
    // Unsupported API by web3j-evm as of 4.8.4
    //
    // eth_protocolVersion
    // eth_coinbase
    // eth_accounts
    
    logger.info("web3_clientVersion: {}", 
        web3j.web3ClientVersion().send().getWeb3ClientVersion());
    //logger.info("eth_protocolVersion: {}", 
    //    web3j.ethProtocolVersion().send().getProtocolVersion());
    //logger.info("eth_coinbase: {}", 
    //    web3j.ethCoinbase().send().getAddress());
    
    logger.info("eth_blockNumber: {}", web3j.ethBlockNumber().send().getBlockNumber());
    //logger.info("admin_nodeInfo: {}", web3j.adminNodeInfo().send().toString());
    
    logger.info("Balance: {}", web3j.ethGetBalance("fe3b557e8fb62b89f4916b721be55ceb828dbd73", 
        DefaultBlockParameterName.LATEST).send().getBalance());
    
    String from = "fe3b557e8fb62b89f4916b721be55ceb828dbd73";
    String byteCd = "608060405234801561001057600080fd5b5060405161040038038061040083398101604052805160008054600160a060020a0319163317905501805161004c906001906020840190610053565b50506100ee565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061009457805160ff19168380011785556100c1565b828001600101855582156100c1579182015b828111156100c15782518255916020019190600101906100a6565b506100cd9291506100d1565b5090565b6100eb91905b808211156100cd57600081556001016100d7565b90565b610303806100fd6000396000f3006080604052600436106100565763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166341c0e1b5811461005b5780634ac0d66e14610072578063cfae3217146100cb575b600080fd5b34801561006757600080fd5b50610070610155565b005b34801561007e57600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100709436949293602493928401919081908401838280828437509497506101929650505050505050565b3480156100d757600080fd5b506100e06101a9565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561011a578181015183820152602001610102565b50505050905090810190601f1680156101475780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60005473ffffffffffffffffffffffffffffffffffffffff163314156101905760005473ffffffffffffffffffffffffffffffffffffffff16ff5b565b80516101a590600190602084019061023f565b5050565b60018054604080516020601f600260001961010087891615020190951694909404938401819004810282018101909252828152606093909290918301828280156102345780601f1061020957610100808354040283529160200191610234565b820191906000526020600020905b81548152906001019060200180831161021757829003601f168201915b505050505090505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061028057805160ff19168380011785556102ad565b828001600101855582156102ad579182015b828111156102ad578251825591602001919060010190610292565b506102b99291506102bd565b5090565b61023c91905b808211156102b957600081556001016102c35600a165627a7a723058201548d914c94a5ad314633a38ea9fe4446a4a618f9023f3fcc95d80e636b434830029";
    
    BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).send().getTransactionCount();
    
    Transaction tx = Transaction.createContractTransaction(from, nonce, gasMgr.getGasPrice(), byteCd);
    String txHash = web3j.ethSendTransaction(tx).send().getTransactionHash();
    
    logger.info("Nonce: {}", nonce);
    logger.info("Tx Hash: {}", txHash);
    
  }
  
}
