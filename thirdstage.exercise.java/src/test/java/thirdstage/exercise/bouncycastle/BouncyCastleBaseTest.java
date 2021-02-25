package thirdstage.exercise.bouncycastle;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


  /**
   * @throws Exception
   *
   * @see https://www.codota.com/web/assistant/code/rs/5c7888c1df79be0001ea7af2#L55
   */
  @Test
  public void testGenerateSecp256k1KeyPairs() throws Exception{

    Security.insertProviderAt(new BouncyCastleProvider(), 1);

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDSA", "BC");
    kpg.initialize(new ECGenParameterSpec("secp256k1"), random);

    final KeyPair kp = kpg.generateKeyPair();

    final Key sk = kp.getPrivate();
    final Key pk = kp.getPublic();

    this.logger.info("Private Key - Algorithm: {}, Encoded Value: {}, Encoded Value Length: {}, Format: {}",
        sk.getAlgorithm(), sk.getEncoded(), sk.getEncoded() == null ? 0 : sk.getEncoded().length, sk.getFormat());
    this.logger.info("Public Key - Algorithm: {}, Encoded Value: {}, Encoded Value Length: {}, Format: {}",
        pk.getAlgorithm(), pk.getEncoded(), pk.getEncoded() == null ? 0 : pk.getEncoded().length, pk.getFormat());

    // https://github.com/web3j/web3j/blob/master/utils/src/main/java/org/web3j/utils/Numeric.java#L235
    // @TODO
  }


  @Test
  public void testCreateBouncyCastleKeyStore() throws Exception{

    Security.insertProviderAt(new BouncyCastleProvider(), 1);
    final KeyStore ks = KeyStore.getInstance("BC");

  }

}
