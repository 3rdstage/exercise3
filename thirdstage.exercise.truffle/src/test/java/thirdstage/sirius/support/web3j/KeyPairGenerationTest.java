package thirdstage.sirius.support.web3j;

import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

public class KeyPairGenerationTest{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  final private SecureRandom random = new SecureRandom();

  final private Base64.Encoder b64Encoder = Base64.getEncoder();
  
  final private Base64.Decoder b64Decoder = Base64.getDecoder();


  @Test
  public void testKeyPairGeneration1() throws Exception{

    ECKeyPair pair = Keys.createEcKeyPair(random);


    String prvKey = Numeric.toHexStringNoPrefixZeroPadded(pair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
    String pubKey = Numeric.toHexStringNoPrefixZeroPadded(pair.getPublicKey(), 128);
    String addr = Keys.toChecksumAddress(Keys.getAddress(pair.getPublicKey()));
    String encPrvKey = b64Encoder.encodeToString(prvKey.getBytes(StandardCharsets.ISO_8859_1));
    String decPrvKey = new String(b64Decoder.decode(encPrvKey), StandardCharsets.ISO_8859_1);

    this.logger.info("Generated Key Pair: ");
    this.logger.info("  Private Key : {}", prvKey);
    this.logger.info("  Public Key : {}", pubKey);
    this.logger.info("  Address : {}", addr);
    this.logger.info("  Encoded Private Key: {}", encPrvKey);
    this.logger.info("  Decoded Private Key: {}", decPrvKey);
    
    Assertions.assertEquals(prvKey, decPrvKey);
    

  }


}
