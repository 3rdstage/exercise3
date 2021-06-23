package thirdstage.exercise.web3j;

import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.web3j.utils.Numeric;

/**
 * @author 3rdstage
 *
 * @see org.web3j.utils.Numeric
 */
@TestInstance(Lifecycle.PER_CLASS)
public class Web3jUtilsNumericTest{

  /**
   * {@link Numeric#toBigInt(String)} would not accept empty or blank string
   *
   * @see org.web3j.utils.Numeric#toBigInt(String)
   */
  @Test
  public void testToBigIntWithEmptyOrBlankString() {

    Assertions.assertThrows(RuntimeException.class, () -> Numeric.toBigInt(""));
    Assertions.assertThrows(RuntimeException.class, () -> Numeric.toBigInt("   "));
  }


  @Test
  public void testToBigIntWith0xPrefix() {

    final BigInteger int0 = Numeric.toBigInt("0x0");
    final BigInteger int1 = Numeric.toBigInt("0x1");

    Assertions.assertEquals(int0, BigInteger.ZERO);
    Assertions.assertEquals(int1, BigInteger.ONE);

  }

  @Test
  public void testToBigIntWith0xPrefixAndLeadingZeros() {

    final BigInteger int0 = Numeric.toBigInt("0x0000");
    final BigInteger int1 = Numeric.toBigInt("0x0001");

    Assertions.assertEquals(int0, BigInteger.ZERO);
    Assertions.assertEquals(int1, BigInteger.ONE);

  }

  @Test
  public void testToBigIntWithout0xPrefix() {

    final BigInteger int0 = Numeric.toBigInt("0");
    final BigInteger int1 = Numeric.toBigInt("1");

    Assertions.assertEquals(int0, BigInteger.ZERO);
    Assertions.assertEquals(int1, BigInteger.ONE);

  }

  @Test
  public void testToBigIntWithout0xPrefixWithLeadingZeros() {

    final BigInteger int0 = Numeric.toBigInt("0000");
    final BigInteger int1 = Numeric.toBigInt("0001");

    Assertions.assertEquals(int0, BigInteger.ZERO);
    Assertions.assertEquals(int1, BigInteger.ONE);

  }


  /**
   * @see org.web3j.utils.Numeric#hexStringToByteArray(String)
   */
  @Test
  @DisplayName("'hexStringToByteArray' method can handle tx hash value with the '0x' prefix.")
  public void testHexStringToByteArrayWith0xPrefix() {

    final String txHash = "0x0974f230a9621c71fe41184253718827e07e1097d62e0e558e2d1f10a515b470";

    final byte[] txHashBytes = Numeric.hexStringToByteArray(txHash);


    Assertions.assertEquals(32, txHashBytes.length, "...");


  }


}
