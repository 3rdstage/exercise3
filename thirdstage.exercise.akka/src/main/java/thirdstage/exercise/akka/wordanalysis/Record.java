package thirdstage.exercise.akka.wordanalysis;

import javax.annotation.concurrent.Immutable;
import org.hibernate.validator.constraints.NotBlank;

@Immutable
public class Record{

   private final String word;

   public String getWord(){ return this.word; }

   private final String sentenceId;

   public String getSentenceId(){ return this.sentenceId; }

   private final String timestamp;

   public String getTimestamp(){ return this.timestamp; }

   public Record(@NotBlank String word, String sentenceId, String timestamp){
      this.word = word;
      this.sentenceId = sentenceId;
      this.timestamp = timestamp;
   }

}
