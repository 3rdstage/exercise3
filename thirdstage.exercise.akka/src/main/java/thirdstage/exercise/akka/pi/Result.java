package thirdstage.exercise.akka.pi;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Result implements java.io.Serializable{

   private final double value;

   public Result(double value){
      this.value = value;
   }

   public double getValue(){
      return this.value;
   }

}
