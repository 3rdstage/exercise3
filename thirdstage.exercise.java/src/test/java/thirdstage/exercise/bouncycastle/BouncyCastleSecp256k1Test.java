package thirdstage.exercise.bouncycastle;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(Lifecycle.PER_CLASS)
public class BouncyCastleSecp256k1Test{

  public final static String SECP256K1 = "secp256k1";

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static SecureRandom random = new SecureRandom();


  @BeforeAll
  public void beforeAll() {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }


  private KeyPair generateSecp256k1KeyPair() throws Exception{

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDSA", "BC");
    kpg.initialize(new ECGenParameterSpec("secp256k1"), random);

    return kpg.generateKeyPair();
  }

  /**
   * @throws Exception
   *
   * @see https://www.codota.com/web/assistant/code/rs/5c7888c1df79be0001ea7af2#L55
   */
  @Test
  public void testGenerateSecp256k1KeyPairs() throws Exception{

    final KeyPair kp = this.generateSecp256k1KeyPair();

    final Key sk = kp.getPrivate();
    final Key pk = kp.getPublic();

    this.logger.info("Private Key - Algorithm: {}, Encoded Value: {}, Encoded Value Length: {}, Format: {}",
        sk.getAlgorithm(), sk.getEncoded(), sk.getEncoded() == null ? 0 : sk.getEncoded().length, sk.getFormat());
    this.logger.info("Public Key - Algorithm: {}, Encoded Value: {}, Encoded Value Length: {}, Format: {}",
        pk.getAlgorithm(), pk.getEncoded(), pk.getEncoded() == null ? 0 : pk.getEncoded().length, pk.getFormat());

    // https://github.com/web3j/web3j/blob/master/utils/src/main/java/org/web3j/utils/Numeric.java#L235
    // @TODO
  }

  /**
   * @throws Exception
   *
   * @see https://docs.oracle.com/javase/9/security/java-cryptography-architecture-jca-reference-guide.htm#JSSEC-GUID-9CF09CE2-9443-4F4E-8095-5CBFC7B697CF
   */
  @Test
  public void testSigningWithSecp256k1KeyPairs() throws Exception{

    final KeyPair kp = this.generateSecp256k1KeyPair();

    final PrivateKey sk = kp.getPrivate();
    final PublicKey pk = kp.getPublic();

    final Signature signer = Signature.getInstance("SHA256withECDSA");
    signer.initSign(sk);

    final String msg1 = "We built this city on rock'n roll.";
    signer.update(msg1.getBytes(StandardCharsets.UTF_8));
    final byte[] sign1 = signer.sign();

    final String msg2 = "Big city night.";
    signer.update(msg2.getBytes(StandardCharsets.UTF_8));
    final byte[] sign2 = signer.sign();


    final Signature verifier = Signature.getInstance("SHA256withECDSA");
    verifier.initVerify(pk);


    verifier.update(msg1.getBytes(StandardCharsets.UTF_8));
    Assertions.assertTrue(verifier.verify(sign1));

    verifier.update(msg2.getBytes(StandardCharsets.UTF_8));
    Assertions.assertTrue(verifier.verify(sign2));

    verifier.update(msg1.getBytes(StandardCharsets.UTF_8));
    Assertions.assertFalse(verifier.verify(sign2));

    verifier.update(msg2.getBytes(StandardCharsets.UTF_8));
    Assertions.assertFalse(verifier.verify(sign1));
  }

  /**
   *
   * @see https://github.com/web3j/web3j/blob/v4.6.4/crypto/src/main/java/org/web3j/crypto/Sign.java
   * @see https://blog.enuma.io/update/2016/11/01/a-tale-of-two-curves-hardware-signing-for-ethereum.html
   * @see https://en.bitcoin.it/wiki/Secp256k1
   */
  @Test
  public void testSecp256k1CurveParams() {
    final X9ECParameters params = CustomNamedCurves.getByName(SECP256K1);

    /**
     * P = 0xFFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE FFFFFC2F
     * a = 0
     * b = 7
     * Gx = 0x79BE667E F9DCBBAC 55A06295 CE870B07 029BFCDB 2DCE28D9 59F2815B 16F81798
     * Gy = 0x483ADA77 26A3C465 5DA4FBFC 0E1108A8 FD17B448 A6855419 9C47D08F FB10D4B8
     * n = 0xFFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE BAAEDCE6 AF48A03B BFD25E8C D0364141
     * h = 01
     */

    BigInteger n0 = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
    BigInteger gx0 = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    BigInteger gy0 = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);

    this.logger.info("n0 : {}", n0);
    this.logger.info("Gx0 : {}", gx0);
    this.logger.info("Gy0 : {}", gy0);

    BigInteger a = params.getCurve().getA().toBigInteger();
    BigInteger b = params.getCurve().getB().toBigInteger();
    BigInteger gx = params.getG().getXCoord().toBigInteger();
    BigInteger gy = params.getG().getYCoord().toBigInteger();
    BigInteger n = params.getN();
    BigInteger h = params.getH();
    byte[] s = params.getSeed();

    this.logger.info("\nsecp256k1 curve");
    this.logger.info("    a : {}", a);
    this.logger.info("    b : {}", b);
    this.logger.info("   Gx : {}", gx);
    this.logger.info("   Gy : {}", gy);
    this.logger.info("    n : {}", n);
    this.logger.info("    h : {}", h);
    this.logger.info(" seed : {}", s);

    Assertions.assertEquals(a, BigInteger.ZERO);
    Assertions.assertEquals(b, BigInteger.valueOf(7L));
    Assertions.assertEquals(gx, gx0);
    Assertions.assertEquals(gy, gy0);
    Assertions.assertEquals(n, n0);
    Assertions.assertEquals(h, BigInteger.ONE);

  }

}
