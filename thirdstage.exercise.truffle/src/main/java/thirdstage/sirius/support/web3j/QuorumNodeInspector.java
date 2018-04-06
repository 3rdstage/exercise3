package thirdstage.sirius.support.web3j;

import java.util.List;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.quorum.Quorum;

public class QuorumNodeInspector{
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private Quorum quorum;
  
  private String coinbase;
  
  private String protocolVer;
  
  private String shhVer;
  
  private String web3Ver;
  
  /**
   * Network ID
   * 
   * @see https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version
   */
  private String netVer;
  
  public QuorumNodeInspector(@Nonnull Quorum qr) {
    Validate.notNull(qr, "Nonnull Quorum object should be provided.");
    
    this.quorum = qr;
  }
  
  public BigInteger getBlockNumber() {
    try {
      BigInteger num = this.quorum.ethBlockNumber().send().getBlockNumber();
      logger.debug("Block number : {}", num.longValue());
      return num;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get block number of Quorum node", ex);
    }
  }
  
  public String getCoinbase() {
    if(this.coinbase != null) return this.coinbase;

    try {
      this.coinbase = this.quorum.ethCoinbase().send().getAddress();
      logger.debug("Coinbase : {}", this.coinbase);
      return this.coinbase;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get coinbase of Quorum node", ex);
    }
  }
  
  public BigInteger getGasPrice() {
    try {
      BigInteger price = this.quorum.ethGasPrice().send().getGasPrice();
      logger.debug("Gas price : {}", price);
      return price;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get gas price of Quorum node", ex);
    }    
  }
  
  public String getProtocolVersion() {
    if(this.protocolVer != null) return this.protocolVer;

    try {
      this.protocolVer = this.quorum.ethProtocolVersion().send().getProtocolVersion();
      logger.debug("Protocol version : {}", this.protocolVer);
      return this.protocolVer;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get protocol version of Quorum node", ex);
    }
  }
  
  public String getNetworkId() {
    if(this.netVer != null) return this.netVer;

    try {
      this.netVer = this.quorum.netVersion().send().getNetVersion();
      logger.debug("Network ID : {}", this.netVer);
      return this.netVer;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get network ID of Quorum node", ex);
    }
  }
  
  public int getPeerCount() {
    try {
      BigInteger cnt = this.quorum.netPeerCount().send().getQuantity();
      logger.debug("Gas price : {}", cnt.intValue());
      return cnt.intValue();
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get peer count of Quorum node", ex);
    }        
  }
  
  public String getWhisperProtocolVersion() {
    if(this.shhVer != null) return this.shhVer;

    try {
      this.shhVer = this.quorum.shhVersion().send().getVersion();
      logger.debug("Whisper protocol version : {}", this.shhVer);
      return this.shhVer;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get whisper protocol version of Quorum node", ex);
    }    
  }
  
  public String getWeb3Version() {
    if(this.web3Ver != null) return this.web3Ver;

    try {
      this.web3Ver = this.quorum.web3ClientVersion().send().getWeb3ClientVersion();
      logger.debug("Web3 version : {}", this.web3Ver);
      return this.web3Ver;
    }catch(IOException ex) {
      throw new RuntimeException("Fail to get web3 client version of Quorum node", ex);
    }    
  }
  
  
}
