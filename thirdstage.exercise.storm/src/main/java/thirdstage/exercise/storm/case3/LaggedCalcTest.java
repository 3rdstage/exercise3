package thirdstage.exercise.storm.case3;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LaggedCalcTest {
   

   @Test
   public void testSumIntBetween1() {

      LaggedCalc calc = new LaggedCalc(500);
      
      long t1 = System.currentTimeMillis();
      long sum = calc.sumIntBetween(1, 10);
      long t2 = System.currentTimeMillis();
      
      Assert.assertEquals(sum, 55);
      Assert.assertTrue(t2 - t1 > 5000);
      
   }
}
