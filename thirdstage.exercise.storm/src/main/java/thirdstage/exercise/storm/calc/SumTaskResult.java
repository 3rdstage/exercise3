package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.storm.commons.lang.builder.StandardToStringStyle;
import org.apache.storm.commons.lang.builder.ToStringBuilder;
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
public class SumTaskResult{

   public enum TaskStatus{
      SUCCESS,
      FAIL
   }

   @XmlElement(name="no")
   private int no;

   @XmlElement(name="status")
   private TaskStatus status;

   @XmlElement(name="sum")
   private long sum = 0;

   private transient String str = null;

   /**
    * private no-arg constructor for Kryo deserilization
    */
   private SumTaskResult(){ }

   @JsonCreator
   public SumTaskResult(@Nullable @JsonProperty("no") int no,
         @Nonnull @JsonProperty("status") TaskStatus status,
         @JsonProperty("sum") long sum){
      this.no = no;
      this.status = status;
      this.sum = sum;
   }

   public int getNo(){ return this.no; }

   public TaskStatus getStatus(){ return this.status; }

   public long getSum(){ return this.sum; }

   @Override
   public String toString(){
      if(this.str == null){
         this.str = new ToStringBuilder(this, StandardToStringStyle.DEFAULT_STYLE)
               .append("no", this.no).append("status", this.status.toString())
               .append("sum", this.sum).toString();
      }

      return this.str;
   }
}
