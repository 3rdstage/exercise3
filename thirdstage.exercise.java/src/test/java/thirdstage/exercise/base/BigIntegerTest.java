package thirdstage.exercise.base;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigIntegerTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testLeftShift() {

    final BigInteger bi = new BigInteger(64, new Random());
    final BigInteger bi2 = bi.shiftLeft(4);
    final BigInteger bi3 = bi.shiftLeft(-4);

    this.logger.debug("Number : 0x{}", bi.toString(16));
    this.logger.debug("Number : 0x{}", bi2.toString(16));
    this.logger.debug("Number : 0x{}", bi3.toString(16));

  }

  @Test
  public void testShift() {

    final BigInteger bi = new BigInteger(64, new Random());
    final BigInteger bi2 = bi.shiftRight(4);
    final BigInteger bi3 = bi.shiftRight(-4);

    this.logger.debug("Number : 0x{}", bi.toString(16));
    this.logger.debug("Number : 0x{}", bi2.toString(16));
    this.logger.debug("Number : 0x{}", bi3.toString(16));

  }

  @Test
  public void testAnd() {

    final BigInteger bi = new BigInteger(64, new Random()).add(BigInteger.valueOf(256*256*256));
    final BigInteger bi2 = bi.and(BigInteger.valueOf(256 -1));
    final BigInteger bi3 = bi.and(BigInteger.valueOf(16).pow(10).subtract(BigInteger.ONE));


    this.logger.debug("Number : 0x{}", bi.toString(16));
    this.logger.debug("Number : 0x{}", bi2.toString(16));
    this.logger.debug("Number : 0x{}", bi3.toString(16));

  }

  @Test
  public void testBitLength() {

    final BigInteger bi1 = BigInteger.TWO.pow(8);

    this.logger.debug("Number: 0x{}, Bit length: {}", bi1.toString(16), bi1.bitLength());
  }

  @Test
  public void testToByteArray() throws Exception{

    final long[] numbers = {255, 322, 4_836_139, 1_789_239_821};

    for(long num: numbers) {

      final BigInteger bi = BigInteger.valueOf(num);
      final byte[] bytes = bi.toByteArray();
      final char[] chars = Hex.encodeHex(bytes);
      this.logger.debug("Number: 0x{}, Byte Array: {}, Char Array: {}",
        bi.toString(16), Arrays.toString(bytes), Arrays.toString(chars));
    }
  }

  @Test
  public void testToByteArray2() throws Exception{

    final BigInteger[] numbers = {
        new BigInteger("11189637300111047250546250645841071037658414346148382668121150328533379449559376256223093354578621540510759848008185390385041014923926395792993359147444520")
    };

    for(BigInteger num: numbers) {

      final byte[] bytes = num.toByteArray();
      final char[] chars = Hex.encodeHex(bytes);
      this.logger.debug("Number: 0x{}, Byte Array: {}, Char Array: {}",
        num.toString(16), Arrays.toString(bytes), Arrays.toString(chars));
    }
  }


  /**
   * @throws Exception
   *
   * @see https://en.bitcoin.it/wiki/Secp256k1
   * @see https://github.com/ethereum/eth-keys/blob/v0.3.3/eth_keys/constants.py
   */
  @Test
  public void testToString() throws Exception{

    // secp256k1 field size
    final String ph = "FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE FFFFFC2F"
        .replaceAll("\\s", ""); // in hexa decimal
    final BigInteger p = BigInteger.TWO.pow(256).subtract(BigInteger.TWO.pow(32)).subtract(BigInteger.valueOf(977));

    // secp256k1 order
    final String nh = "FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE BAAEDCE6 AF48A03B BFD25E8C D0364141"
        .replaceAll("\\s", ""); //in hexadecimal
    final BigInteger n = new BigInteger(nh, 16);
    final String nd = "115792089237316195423570985008687907852837564279074904382605163141518161494337";

    Assertions.assertEquals(ph, p.toString(16).toUpperCase());
    Assertions.assertEquals(nd, n.toString());

    this.logger.debug("Field Size (2^256 - 2^32 - 977): {}", p.toString());
    this.logger.debug("Order ({}): {}", nh, n.toString());

  }


}
