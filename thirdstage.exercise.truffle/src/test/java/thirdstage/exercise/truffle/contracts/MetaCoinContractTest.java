package thirdstage.exercise.truffle.contracts;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.web3j.abi.datatypes.Type;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MetaCoinContractTest{
  
  
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  MetaCoinContract contract;


  @Test
  public void testBase() {
    String contractAddr = contract.getAddress();
    String from = contract.getFrom();
    

    this.logger.info("Contract Address: {}, From: {}", contractAddr, from);

    Assertions.assertTrue(StringUtils.isNotBlank(contractAddr));
    Assertions.assertTrue(StringUtils.isNotBlank(from));
  }
  
  @Test
  public void testGetBalance() {
    final String addr = contract.getFrom();
    
    BigInteger bal = contract.getBalance(addr);
    
    this.logger.info("Output: {}", bal);
    Assertions.assertTrue(bal.longValue() > 5000);
  }
  
}
