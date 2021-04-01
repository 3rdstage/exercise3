package thirdstage.exercise.bouncycastle;

import java.security.KeyStore;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 3rdstage
 *
 * @see https://github.com/web3j/web3j/blob/v4.6.4/crypto/src/main/java/org/web3j/crypto/Keys.java#L65
 * @see https://github.com/web3j/web3j/blob/v4.6.4/crypto/src/main/java/org/web3j/crypto/Sign.java
 */
@TestInstance(Lifecycle.PER_CLASS)
public class BouncyCastleBaseTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static SecureRandom random = new SecureRandom();


  @Test
  public void testBouncyCastleInit() throws Exception{

    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  @Test
  public void testListSecurityProviders() {

    final int index = Security.insertProviderAt(new BouncyCastleProvider(), 1);
    logger.info("Bouncy Castle provider is installed at {}", index);

    final Provider[] providers = Security.getProviders();

    logger.info("\nInstalled Security Providers : {}", providers.length);
    for(Provider provider: providers) {
      this.logger.info("  {}: {}", provider.getName(), provider.getInfo());
    }
  }

  @Test
  public void testListAlgorithms() {

    Security.insertProviderAt(new BouncyCastleProvider(), 1);
    final String[] services = {"Signature", "MessageDigest", "Cipher", "Mac", "KeyStore"};

    logger.info("\nInstalled Algorithms");
    Set<String> algorithms = null;
    for(String svc: services) {
      algorithms = Security.getAlgorithms(svc);

      for(String algr: algorithms) {
        this.logger.info("{} - {}", svc, algr);
      }

    }
  }

  @Test
  public void testCreateBouncyCastleKeyStore() throws Exception{

    Security.insertProviderAt(new BouncyCastleProvider(), 1);
    final KeyStore ks = KeyStore.getInstance("BC");

  }

}
