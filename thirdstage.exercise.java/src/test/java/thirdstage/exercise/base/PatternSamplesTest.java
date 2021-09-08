package thirdstage.exercise.base;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatternSamplesTest{

  private final PatternSamples testee = new PatternSamples();

  @Test
  public void testMaskSubscriberNoOnly() {

    final String unmasked = "12345(?) 223334($) 892356(@@)";
    final String masked = testee.maskSubscriberNoOnly(unmasked);


    Assertions.assertEquals(unmasked.length(), masked.length());
    Assertions.assertEquals(StringUtils.countMatches(unmasked, '('),
        StringUtils.countMatches(masked, '('));
    Assertions.assertEquals(StringUtils.countMatches(unmasked, ')'),
        StringUtils.countMatches(masked, ')'));
    Assertions.assertEquals(unmasked.substring(5, 8), masked.substring(5, 8));
    Assertions.assertEquals(unmasked.substring(25, 29), masked.substring(25, 29));
    Assertions.assertNotEquals(masked, unmasked);



  }

}
