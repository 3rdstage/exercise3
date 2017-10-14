package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thirdstage.exercise.storm.calc.SumTaskResult.TaskStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SumTaskBolt extends BaseBasicBolt {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   private ObjectMapper mapper = null;

   @Override
   public void prepare(Map conf, TopologyContext cntx){
      super.prepare(conf, cntx);
      this.mapper = new ObjectMapper();
      mapper.registerModule(new JaxbAnnotationModule());
   }

   @Override
   public void execute(Tuple input, BasicOutputCollector collector) {
      String retInfo = input.getStringByField("return-info");
      String jobId = input.getStringByField("job-id");
      int tasks = input.getIntegerByField("tasks-total");
      SumTaskRequest req = (SumTaskRequest)input.getValueByField("task-req");
      logger.debug("SumTaskBolt - Received sum task: {}", req);

      SumTaskResult result = null;
      String resultStr = null;

      try{
         LazyCalc calc = new LazyCalc(req.getDelay());

         long sum = calc.sumIntBetween(req.getFrom(), req.getTo(), req.getStep());
         result = new SumTaskResult(req.getNo(), TaskStatus.SUCCESS, sum);
      }catch(Exception ex){
         logger.error("Fail to execute sum task request.", ex);
         result = new SumTaskResult((req != null) ? req.getNo() : -1, TaskStatus.FAIL, 0);
      }

      try{
         resultStr = mapper.writeValueAsString(result);
         collector.emit(new Values(retInfo, jobId, tasks,
               result.getNo(), result.getStatus(), result));
         logger.debug("SumTaskBolt - Emitted sum task result: {}", resultStr);
      }catch(Exception ex){
         logger.error("Fail to serialize sum task result.", ex);
         collector.emit(new Values(retInfo, jobId, tasks,
               result.getNo(), result.getStatus(), null));
      }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("return-info", "job-id", "tasks-total",
            "task-no", "task-status", "task-result"));

   }



}
