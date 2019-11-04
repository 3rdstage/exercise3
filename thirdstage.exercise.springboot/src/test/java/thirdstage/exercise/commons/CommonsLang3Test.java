package thirdstage.exercise.commons;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonsLang3Test{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testArrayUtilsToString1() {

    final String[] arr1 = {"Apple", "Banana", "Kiwi"};
    final String str1 = ArrayUtils.toString(arr1);
    logger.info(str1);

    final String[] arr2 = null;
    final String str2 = ArrayUtils.toString(arr2);
    logger.info(str2);

  }


}
