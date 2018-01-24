package thirdstage.exercise.web3j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

@Test
public class SimpleTestnetTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public void testClientVersion() throws Exception{
    Web3j client = Web3j.build(new HttpService());
    Web3ClientVersion clientVersion = client.web3ClientVersion().send();
    String ver = clientVersion.getWeb3ClientVersion();

    logger.info("The version of Ethereum client is : {}", ver);

    Assert.assertNotNull(ver);
    Assert.assertTrue(ver.startsWith("Geth"));
  }

}
