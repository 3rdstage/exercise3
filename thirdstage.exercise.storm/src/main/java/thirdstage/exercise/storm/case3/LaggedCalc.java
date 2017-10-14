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
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


@ThreadSafe
public class LaggedCalc{

   public static final int DEFAULT_DELAY = 500;

   private int delay = 500;

   public LaggedCalc(){}

   /**
    * @param delay in millisecond
    */
   public LaggedCalc(int delay){
      this.delay = delay;
   }


   public long sumIntBetween(int from, int to){
      int a = from, b = to;
      long s = 0;
      if(from > to){
         a = to;
         b = from;
      }

      for(int i = a; i < b; i++){
         try{ Thread.currentThread().sleep(this.delay); }
         catch(InterruptedException ex){}

         s += i;
      }
      try{ Thread.currentThread().sleep(this.delay); }
      catch(InterruptedException ex){}
      return (s + b);
   }
}
