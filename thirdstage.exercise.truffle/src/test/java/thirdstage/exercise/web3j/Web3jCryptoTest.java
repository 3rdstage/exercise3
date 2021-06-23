package thirdstage.exercise.web3j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

@TestInstance(Lifecycle.PER_CLASS)
public class Web3jCryptoTest{

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

  @Test
  @DisplayName("It is safe to apply 'Keys.toChecksumAddress()' method to a string already being checksum address.")
  public void testKeysToChecksumAddress1() throws Exception{

    final ECKeyPair pair = Keys.createEcKeyPair(random);

    final String addr1 = Keys.getAddress(pair);
    final String addr2 = Keys.toChecksumAddress(addr1);
    final String addr3 = Keys.toChecksumAddress(addr2);

    Assertions.assertNotEquals(addr1.substring(0, 2), "0x");
    Assertions.assertEquals(addr2.substring(0, 2), "0x");
    Assertions.assertEquals(addr2, addr3);

  }

  @Test
  @DisplayName("'Keys.toChecksumAddress()' can handle address strings whose lengths are less than 40 and don't start with '0x'")
  public void testKeysToChecksumAddress2() {

    final String addr1 = "0";
    final String addr2 = "00";
    final String addr3 = "001";

    final String addr11 = Keys.toChecksumAddress(addr1);
    final String addr12 = Keys.toChecksumAddress(addr2);
    final String addr13 = Keys.toChecksumAddress(addr3);

    this.logger.info("addr: {}, 'Keys.toChecksumAddress(addr)': {}", addr1, addr11);
    this.logger.info("addr: {}, 'Keys.toChecksumAddress(addr)': {}", addr2, addr12);
    this.logger.info("addr: {}, 'Keys.toChecksumAddress(addr)': {}", addr3, addr13);


  }


  @Test
  public void testTransactionEncoderSignTransaction1() {

    final String signerAddr = "0xaCEB106d13Ada89057C0FC41f6AbaA3Ad78265DC";
    final String signerPrvKey = "79999a765060abafb3c9b93ebd6000da16e1dac64221ff3ea9311dbd1851734f";
    final Credentials signerCred = Credentials.create(signerPrvKey);

    final BigInteger nonce = Numeric.toBigInt("0x9");
    final BigInteger from = Numeric.toBigInt("0xaCEB106d13Ada89057C0FC41f6AbaA3Ad78265DC");
    final BigInteger gas = Numeric.toBigInt("0x12e140");
    final BigInteger gasPrice = Numeric.toBigInt("0x0");
    final String to = "0xd9A9836042e02822468B1973B98074201a45DacC";
    final String data = "0xd10e73ab00000000000000000000000011966681ec9d3fccf3cfe2dc7b203a73720df539000000000000000000000000e3561c201eae61271b99ab17e33804fc1a48e257";

    final RawTransaction rawTx = RawTransaction.createTransaction(nonce, gasPrice, gas, to, data);
    final String signedMsg = Numeric.toHexString(TransactionEncoder.signMessage(rawTx, signerCred));

    this.logger.info("Signed Message: {}", signedMsg);

    final String expected = "0xf8a509808312e14094d7219cca20b89ff9fa28c3939c9b99a2f495235480b844d10e73ab00000000000000000000000011966681ec9d3fccf3cfe2dc7b203a73720df539000000000000000000000000e3561c201eae61271b99ab17e33804fc1a48e2571ca06a48880f4798628b3fa34c198696bbdeae203fe79eb131a424f033aa070af73da05d003a93d0a05fe7b1d41dc598cc03d0e1043ecfb52f5df1323ed923c1a51e2a";

    Assertions.assertEquals(expected, signedMsg);

  }



}
