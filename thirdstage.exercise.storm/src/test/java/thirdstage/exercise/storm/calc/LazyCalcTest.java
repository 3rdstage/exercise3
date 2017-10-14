package thirdstage.exercise.storm.calc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LazyCalcTest {

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testSumIntBetween1() {

      LazyCalc calc = new LazyCalc(0);

      int a = 1;
      int b = 100;
      int step = 1;
      long sum = calc.sumIntBetween(a, b, step);

      this.logger.info("Sum integers between 0 to 100 is : {}", sum);

      Assert.assertEquals(sum, 5050);

   }

   @Test
   public void testSumIntBetween2() {

      LazyCalc calc = new LazyCalc(50);

      int a = 1;
      int b = 100;
      int step = 1;
      long startAt = System.currentTimeMillis();
      long sum = calc.sumIntBetween(a, b, step);
      long endAt = System.currentTimeMillis();

      this.logger.info("Sum integers btw. 0 and 100: {}, duration: {}", sum, (endAt - startAt));

      Assert.assertEquals(sum, 5050);

   }
}
