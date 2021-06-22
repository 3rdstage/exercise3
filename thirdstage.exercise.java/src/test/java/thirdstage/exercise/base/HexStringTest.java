package thirdstage.exercise.base;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.xml.bind.DatatypeConverter;

public class HexStringTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Test
  public void testHexStringToString() {

    final String hStr = "0x477265656e";
    final byte[] bytes = DatatypeConverter.parseHexBinary(
        StringUtils.removeStart(hStr, "0x"));

    final String str = new String(bytes);
    Assertions.assertEquals("Green", str);

    this.logger.info("Hex Binary Representation: '{}'", hStr);
    this.logger.info("Byte Array: {}", ArrayUtils.toString(bytes));
    this.logger.info("String: '{}'", str);

    final String hStr2 = "0x5768697465";
    final byte[] bytes2 = DatatypeConverter.parseHexBinary(
        StringUtils.removeStart(hStr2, "0x"));

    final String str2 = new String(bytes2);
    Assertions.assertEquals("White", str2);

    this.logger.info("Hex Binary Representation: '{}'", hStr2);
    this.logger.info("Byte Array: {}", ArrayUtils.toString(bytes2));
    this.logger.info("String: '{}'", str2);

  }

  @Test
  public void testHexStringToString2() {

    final String hStr = "0x477265656e000000000000000000000000000000000000000000000000000000";
    final byte[] bytes = DatatypeConverter.parseHexBinary(
        StringUtils.removeStart(hStr, "0x"));

    final String str = new String(bytes);
    Assertions.assertNotEquals("Green", str);
    Assertions.assertEquals(32, str.length());
    Assertions.assertEquals("Green", str.trim());

    this.logger.info("Hex Binary Representation: '{}'", hStr);
    this.logger.info("Byte Array: {}", ArrayUtils.toString(bytes));
    this.logger.info("String: '{}'", str);

    final String hStr2 = "0x5768697465000000000000000000000000000000000000000000000000000000";
    final byte[] bytes2 = DatatypeConverter.parseHexBinary(
        StringUtils.removeStart(hStr2, "0x"));

    final String str2 = new String(bytes2);
    Assertions.assertNotEquals("White", str2);
    Assertions.assertEquals(32, str2.length());
    Assertions.assertEquals("White", str2.trim());

    this.logger.info("Hex Binary Representation: {}", hStr2);
    this.logger.info("Byte Array: {}", ArrayUtils.toString(bytes2));
    this.logger.info("String: {}", str2);

  }

}
