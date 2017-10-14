package thirdstage.exercise.storm.calc;

import java.io.PrintStream;
import java.net.InetAddress;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;

import org.apache.storm.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thirdstage.exercise.storm.calc.SumTaskResult.TaskStatus;
import backtype.storm.Config;
import backtype.storm.ILocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class CalcTopologyLauncher {

   public static final String DEFAULT_NAME = "CalcTopology";

   private static Logger logger = LoggerFactory.getLogger(CalcTopologyLauncher.class);

   public enum Function{
      SUM,
      PRIME,
   }

   protected static StormTopology buildTopology(@Nullable ILocalDRPC localDrpc){
      return buildCalcTopology(2, 5, 2, 2, localDrpc);
   }

   protected static StormTopology buildCalcTopology(
         @Min(1) int sumJobBoltNum, @Min(3) int sumTaskBoltNum,
         @Min(1) int sumJoinBoltNum, @Min(1) int resultBoltNum,
         @Nullable ILocalDRPC localDrpc){
      Validate.isTrue(sumJobBoltNum > 0, "Invalid number of SumJobBolt is specified.");
      Validate.isTrue(sumTaskBoltNum > 2, "At least 3 SumTaskBolt should be specified.");
      Validate.isTrue(sumJoinBoltNum > 0, "Invalid number of SumJobBolt is specified.");
      Validate.isTrue(resultBoltNum > 0, "Invalid number of SumJobBolt is specified.");

      TopologyBuilder builder = new TopologyBuilder();
      
      DRPCSpout sumSpt = null;
      if(localDrpc != null){
         //@TODO Find out the way to add LocalDRPC after building topology
         sumSpt = new DRPCSpout(Function.SUM.name(), localDrpc);
      }else{
         sumSpt = new DRPCSpout(Function.SUM.name());
      }

      builder.setSpout("drpc-sum", sumSpt);
      builder.setBolt("sum-job", new SumJobBolt(), sumJobBoltNum).shuffleGrouping("drpc-sum");
      builder.setBolt("sum-task", new SumTaskBolt(), sumTaskBoltNum).shuffleGrouping("sum-job");
      builder.setBolt("sum-task-result-join", new SumTaskResultJoinBolt(), sumJoinBoltNum)
      .fieldsGrouping("sum-task", new Fields("job-id"));
      builder.setBolt("sum-return", new ReturnResults(), resultBoltNum).shuffleGrouping("sum-task-result-join");

      return builder.createTopology();
   }

   public static void printUsage(PrintStream out){

      out.println("Specify the topology name in command line.");

   }

   public static void main(String... args) throws Exception{

      if(args == null || args.length == 0){
         printUsage(System.out);
         return;
      }

      String name = args[0];

      StormTopology topology = buildTopology(null);
      Config conf = new Config();
      conf.setFallBackOnJavaSerialization(false);
      conf.registerSerialization(SumTaskRequest.class);
      conf.registerSerialization(SumTaskResult.class);
      conf.registerSerialization(TaskStatus.class);
      conf.setNumWorkers(3);

      try{
         logger.info("Submitting storm topology(name: {}) at {}", name, InetAddress.getLocalHost().getHostName());
         StormSubmitter.submitTopology(name, conf, topology);
      }catch(Exception ex){
         logger.error("Fail to submit the topology.", ex);
         throw ex;
      }
   }

}
