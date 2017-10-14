package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thirdstage.exercise.storm.calc.SumJobResult.JobStatus;
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

public class SumTaskResultJoinBolt extends BaseBasicBolt {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   private ObjectMapper mapper = null;

   //@TODO Need more consideration on setting 'results' static.
   private final Map<String, SumJobResult> results = new HashMap<>();

   private final Lock resultsLock = new ReentrantLock();

   @Override
   public void prepare(Map conf, TopologyContext cntx){

      //@TODO Register the Jaskson mapper to the platform mbean server.
      super.prepare(conf, cntx);
      this.mapper = new ObjectMapper();
      mapper.registerModule(new JaxbAnnotationModule());
   }

   @Override
   public void cleanup(){
      results.clear();
      super.cleanup();
   }

   @Override
   public void execute(Tuple input, BasicOutputCollector collector) {

      String retInfo = input.getStringByField("return-info");
      String jobId = input.getStringByField("job-id");
      int tasks = input.getIntegerByField("tasks-total");
      int taskNo = input.getIntegerByField("task-no");
      TaskStatus taskStatus =(TaskStatus)input.getValueByField("task-status");
      SumTaskResult result = (SumTaskResult)input.getValueByField("task-result");

      logger.debug("SumTaskResultJoinBolt - Received sum task result: {}", result);

      SumJobResult jobResult = null;
      resultsLock.lock();
      try{
         jobResult = results.get(jobId);
         if(jobResult == null){
            jobResult = new SumJobResult(jobId, tasks, 0, 0, JobStatus.RUNNING, 0);
            results.put(jobId, jobResult);
         }

         if(taskStatus == TaskStatus.FAIL || result == null){
            jobResult.increaseTasksFail(1);
         }else{
            jobResult.increaseTasksSuccess(1).addSum(result.getSum());
         }
         if(tasks == (jobResult.getTasksSuccess() + jobResult.getTasksFail())){
            //all tasks are processed
            jobResult.setStatus(JobStatus.COMPLETED);
            String jobResultStr = null;
            try{
               jobResultStr = mapper.writeValueAsString(jobResult);
            }catch(Exception ex){
               logger.error("Fail to serialize job result: {}", jobResultStr);
               jobResultStr = null;
            }
            collector.emit(new Values(jobResultStr, retInfo));
            results.remove(jobId);
            logger.debug("SumTaskResultJoinBolt - Emitted sum job result: {}", jobResultStr);
         }
      }finally{
         resultsLock.unlock();
      }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("result", "return-info"));
   }
}
