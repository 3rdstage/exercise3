package thirdstage.exercise.storm.calc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import thirdstage.exercise.storm.calc.CalcTopologyLauncher.Function;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class LocalCalcTopologyTest {

   private Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testLocalCalcTopology1() throws Exception{

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JaxbAnnotationModule());

      LocalDRPC drpc = new LocalDRPC();
      StormTopology topology = CalcTopologyLauncher.buildTopology(drpc);

      LocalCluster cluster = new LocalCluster();
      Config conf = new Config();
      conf.setFallBackOnJavaSerialization(false);
      conf.registerSerialization(SumTaskRequest.class);
      conf.registerSerialization(SumTaskResult.class);
      cluster.submitTopology(CalcTopologyLauncher.DEFAULT_NAME, conf, topology);

      SumJobRequest req1 = new SumJobRequest("R001", 1, 100, 1, 3, SumJobRequest.DEFAULT_DELAY);
      String reqStr1 = mapper.writeValueAsString(req1);

      SumJobRequest req2 = new SumJobRequest("R002", 101, 200, 1, 3, SumJobRequest.DEFAULT_DELAY);
      String reqStr2 = mapper.writeValueAsString(req2);

      logger.info(drpc.execute(Function.SUM.name(), reqStr1));
      logger.info(drpc.execute(Function.SUM.name(), reqStr2));

      cluster.shutdown();
      drpc.shutdown();
   }

}