package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Min;
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

@NotThreadSafe
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class SumJobResult{

   public enum JobStatus{
      RUNNING,
      COMPLETED,
      FAILED
   }

   /**
    * Job ID
    */
   @XmlElement(name="id")
   private String id;

   /**
    * Total number of tasks
    */
   @XmlElement(name="tasks-total")
   private int tasksTotal;

   /**
    * Number of succeeded tasks
    */
   @XmlElement(name="tasks-success")
   private int tasksSuccess;

   /**
    * Number of failed tasks
    */
   @XmlElement(name="tasks-fail")
   private int tasksFail;

   @XmlElement(name="status")
   private JobStatus status;

   /**
    *
    */
   @XmlElement(name="sum")
   private long sum;

   private transient String str = null;

   /**
    * private no-arg constructor for Kryo deserilization
    */
   private SumJobResult(){}

   @JsonCreator
   public SumJobResult(@Nonnull @JsonProperty("id") String id,
         @Min(0) @JsonProperty("tasks-total") int total,
         @Min(0) @JsonProperty("tasks-success") int successes,
         @Min(0) @JsonProperty("tasks-fail") int fails,
         @JsonProperty("status") JobStatus status,
         @JsonProperty("sum") long sum){
      this.id = id;
      this.tasksTotal = total;
      this.tasksSuccess = successes;
      this.tasksFail = fails;
      this.status = status;
      this.sum = sum;
   }

   public String getId(){ return this.id; }

   public int getTasksTotal(){ return this.tasksTotal; }

   public int getTasksSuccess(){ return this.tasksSuccess; }

   public SumJobResult setTasksSuccess(@Min(0) int num){
      this.tasksSuccess = num;
      return this;
   }

   public SumJobResult increaseTasksSuccess(@Min(1) int delta){
      return this.setTasksSuccess(this.tasksSuccess + delta);
   }

   public int getTasksFail(){ return this.tasksFail; }

   public SumJobResult setTasksFail(@Min(0) int num){
      this.tasksFail = num;
      return this;
   }

   public SumJobResult increaseTasksFail(@Min(1) int delta){
      return this.setTasksFail(this.tasksFail + delta);
   }

   public JobStatus getStatus(){ return this.status; }

   public SumJobResult setStatus(JobStatus status){
      this.status = status;
      return this;
   }

   public long getSum(){ return this.sum; }

   public SumJobResult setSum(long sum){
      this.sum = sum;
      return this;
   }

   public SumJobResult addSum(long delta){
      this.sum = this.sum + delta;
      return this;
   }

   @Override
   public String toString(){
      if(this.str == null){
         this.str = new ToStringBuilder(this, StandardToStringStyle.DEFAULT_STYLE)
               .append("id", this.id).append("tasks-total", this.tasksTotal)
               .append("tasks-success", this.tasksSuccess)
               .append("tasks-fail", this.tasksFail)
               .append("sum", this.sum).toString();
      }

      return this.str;
   }


}
