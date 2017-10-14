package thirdstage.exercise.akka.wordanalysis;

import javax.annotation.concurrent.Immutable;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.Key;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.Keyed;

/**
 * @author Sangmoon Oh
 *
 */
@Immutable
public class Sentence implements Keyed<String>, java.io.Serializable{

   //@TODO Try to separator the Keyed from the Message using decorator pattern or something like that

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final String id;

   public String getId(){ return this.id; }

   private final String sourceId;

   @NotBlank public String getSourceId(){ return this.sourceId; }

   private final Key<String> key;

   @Override
   public Key<String> getKey(){ return this.key; }

   private final String text;

   public String getText(){ return this.text; }

   @JsonCreator
   public Sentence(@JsonProperty("id") @NotBlank String id,
         @JsonProperty("sourceId") @NotBlank String sourceId, @JsonProperty("text") String text){
      this.id = id;
      this.sourceId = sourceId;
      this.text = text;
      this.key = new Key<String>(sourceId);
   }

}
