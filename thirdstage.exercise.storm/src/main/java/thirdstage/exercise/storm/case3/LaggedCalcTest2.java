package thirdstage.exercise.storm.case3;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LaggedCalcTest2{
   
   public static void main(String[] args){
      
      LaggedCalc calc = new LaggedCalc(200);
      int from = 1, to = 100;
      
      long t1 = System.currentTimeMillis();
      long sum = calc.sumIntBetween(from, to);
      long t2 = System.currentTimeMillis();
      
      System.out.printf("The sum of integers between %1$,d and %2$,d using LaggedCalc is %3$,d.\n", from, to, sum);
      System.out.printf("The time duration for summation is %1$,d milliseconds.\n", (t2 - t1));
      
      
   }

}
