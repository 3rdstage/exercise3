package thirdstage.exercise.akka.pi;

import org.testng.annotations.Test;

public class PiTest {

   @Test
   public void testCalculate1() {

      Pi pi = new Pi();

      pi.calculate(4, 10000, 10000);
   }
}
