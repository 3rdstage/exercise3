package thirdstage.exercise.akka.pi;

import javax.annotation.concurrent.Immutable;
import scala.concurrent.duration.Duration;

@Immutable
public class PiApproximation implements java.io.Serializable {

   private final double pi;

   private final Duration duration;


   public PiApproximation(double pi, Duration duration){
      this.pi = pi;
      this.duration = duration;
   }

   public double getPi(){ return this.pi; }

   public Duration getDuration(){ return this.duration; }

}
