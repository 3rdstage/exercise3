package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@Immutable
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class SumJobRequest{

   /**
    * default delay in millisecond.
    */
   public static final int DEFAULT_DELAY = 0;

   @XmlElement(name="id")
   private String id;

   @XmlElement(name="from")
   private int from;

   @XmlElement(name="to")
   private int to;

   @XmlElement(name="step")
   private int step;

   /**
    * number of partitions
    */
   @XmlElement(name="partitions")
   private int partitions;

   /**
    * delay in millisecond
    */
   @XmlElement(name="delay")
   private int delay = DEFAULT_DELAY;

   /**
    * private no-arg constructor for Kryo deserilization
    */
   private SumJobRequest(){}

   /**
    * @param id
    * @param from
    * @param to
    * @param step
    * @param delay
    */
   @JsonCreator
   public SumJobRequest(@Nonnull @JsonProperty("id") String id,
         @JsonProperty("from") int from, @JsonProperty("to") int to,
         @JsonProperty("step") @Min(1) int step,
         @JsonProperty("partitions") @Min(1) int partitions,
         @JsonProperty("delay") @Min(0) int delay){
      Validate.isTrue(to >= from, "The parameter 'to' should be equal or greater than 'from'.");
      Validate.isTrue(step > 0.0, "The parameter 'step' should be positive.");
      Validate.isTrue(partitions > 0, "The parameter 'partitions' should be positive integer.");
      Validate.isTrue(delay >= 0, "The parameter 'delay' should be positive integer.");

      this.id = id;
      this.from = from;
      this.to = to;
      this.step = step;
      this.partitions = partitions;
      this.delay = delay;
   }

   public String getId(){ return this.id; }

   public int getFrom(){ return this.from; }

   public int getTo(){ return this.to; }

   public int getStep(){ return this.step; }

   public int getPartitions(){ return this.partitions; }

   public int getDelay(){ return this.delay; }
}
