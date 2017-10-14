package thirdstage.exercise.akka.pi;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Work implements java.io.Serializable {

   private final int start;

   private final int numOfElements;

   public Work(int start, int elements){
      this.start = start;
      this.numOfElements = elements;
   }

   public int getStart(){ return this.start; }

   public int getNumOfElements(){ return this.numOfElements; }


}
