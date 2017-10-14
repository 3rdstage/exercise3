package thirdstage.exercise.storm.calc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

@ThreadSafe
public class LazyCalc {

   /**
    * Default delay in millisecond
    */
   public static final int DEFAULT_DELAY = 500;

   /**
    * Max dealy in millisecond
    */
   public static final int MAX_DELAY = 5000;

   private int delay = 500;



   /**
    * @param delay in millisecond
    */
   public LazyCalc(@Min(0) @Max(MAX_DELAY) int delay){
      Validate.isTrue(delay > -1, "Delay should be non-negative.");
      Validate.isTrue(delay <= MAX_DELAY, "Delay should be equal or less than %1$s", MAX_DELAY);
      this.delay = delay;
   }


   /**
    * @param from always inclusive
    * @param to should be equal or greater than {@code from} and inclusiveness depends on {@code from} and {@code step}
    * @param step should be positive
    * @return
    * @throws RuntimeException
    */
   public long sumIntBetween(int from, int to, @Min(1) int step, boolean inStorm){
      Validate.isTrue(to >= from, "Parameter 'to' should be equal or greater than the parameter 'from'.");
      Validate.isTrue(step > 0, "Parameter 'step' should be positive.");

      long sum = 0;

      int cur = from;
      for(;;){
         if(delay > 0){
            if(inStorm){
               Utils.sleep(delay);
            }else{
               try{ Thread.sleep(this.delay); }
               catch(InterruptedException ex){
                  throw new RuntimeException(ex);
               }
            }
         }

         sum = sum + cur;
         cur = cur + step;
         if(cur > to){ break; }
      }

      return sum;
   }


   /**
    * @param from always inclusive
    * @param to should be equal or greater than {@code from} and inclusiveness depends on {@code from} and {@code step}
    * @param step should be positive
    * @return
    * @throws RuntimeException
    */
   public long sumIntBetween(int from, int to, @Min(1) int step){
      return this.sumIntBetween(from, to, step, true);
   }

}
