package thirdstage.exercise.akka.wordstats;

import javax.annotation.concurrent.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public interface StatsMessages{

   @Immutable
   public static class StatsJob implements java.io.Serializable{

      private final String id;

      private final String text;

      @JsonCreator
      public StatsJob(@JsonProperty("id") String id, @JsonProperty("text") String text){
         this.id = id;
         this.text = text;
      }

      public String getId(){ return this.id; }

      public String getText(){ return this.text; }
   }

   @Immutable
   public static class StatsTask implements java.io.Serializable{

      private final String jobId;

      private final int taskNo;

      private final String word;

      public StatsTask(String jobId, int taskNo, String word){
         this.jobId = jobId;
         this.taskNo = taskNo;
         this.word = word;
      }

      public String getJobId(){ return this.jobId; }

      public int getTaskNo(){ return this.taskNo; }

      public String getWord(){ return this.word; }
   }


   public static class StatsResult implements java.io.Serializable{

      private final double meanLen;

      public StatsResult(double len){ this.meanLen = len; }

      public double getMeanWordLength(){ return this.meanLen; }

      @Override
      public String toString(){
         return "Mean Word Length : " + this.meanLen;
      }
   }

   public static class StatsJobFailed implements java.io.Serializable{

      private final String reason;

      public String getReason(){ return this.reason; }

      public StatsJobFailed(String reason){ this.reason = reason; }

      @Override
      public String toString(){
         return "Job failed : " + this.reason;
      }
   }

}
