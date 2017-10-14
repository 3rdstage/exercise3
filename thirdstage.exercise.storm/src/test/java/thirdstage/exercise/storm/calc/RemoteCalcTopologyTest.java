package thirdstage.exercise.storm.calc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import thirdstage.exercise.storm.calc.CalcTopologyLauncher.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import backtype.storm.Config;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.DRPCClient;

public class RemoteCalcTopologyTest {

   public static final String SYS_PROP_DRPC_ADDRESS = "exercise.storm.drpc.address";
   public static final String SYS_PROP_DRPC_PORT = "exercise.storm.drpc.port";

   private Logger logger = LoggerFactory.getLogger(this.getClass());

   private String address;

   private int port;

   private ObjectMapper mapper;

   @BeforeClass
   public void beforeClass(){
      String addr = System.getProperty(SYS_PROP_DRPC_ADDRESS);
      if(addr != null){ this.address = addr; }
      else{ this.address = "203.235.192.39"; }

      int port = NumberUtils.toInt(System.getProperty(SYS_PROP_DRPC_PORT), -1);
      if(port > 0){ this.port = port; }
      else{ this.port = 3772; }

      this.mapper = new ObjectMapper();
      mapper.registerModule(new JaxbAnnotationModule());
   }

   @Test
   public void testSum1() throws Exception{

      Config conf = new Config();
      conf.setFallBackOnJavaSerialization(false);
      conf.registerSerialization(SumTaskRequest.class);
      conf.registerSerialization(SumTaskResult.class);
      conf.put(Config.STORM_THRIFT_TRANSPORT_PLUGIN, "backtype.storm.security.auth.SimpleTransportPlugin");
      conf.put(Config.STORM_NIMBUS_RETRY_TIMES, 3);
      conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL, 1000);
      conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL_CEILING, 2000);
      conf.put(Config.DRPC_MAX_BUFFER_SIZE, 3000);
      DRPCClient client = new DRPCClient(conf, this.address, this.port);

      //1st test
      String jobId = "R001";
      int from = 1;
      int to = 100;
      int step = 1;
      int partitions = 3;
      int delay = 50;

      long sum = this.calcSumRemote(client, jobId, from, to, step, partitions, delay);
      long sum0 = this.calcSumSimple(from, to, step, delay);
      Assert.assertEquals(sum, sum0);

      //2nd test
      jobId = "R002";
      from = 1;
      to = 1000;
      step = 1;
      partitions = 30;
      delay = 50;

      sum = this.calcSumRemote(client, jobId, from, to, step, partitions, delay);
      sum0 = this.calcSumSimple(from, to, step, delay);
      Assert.assertEquals(sum, sum0);

      //3rd test
      jobId = "R002";
      from = 1;
      to = 10000;
      step = 1;
      partitions = 100;
      delay = 50;

      sum = this.calcSumRemote(client, jobId, from, to, step, partitions, delay);
      sum0 = this.calcSumSimple(from, to, step, delay);
      Assert.assertEquals(sum, sum0);
   }

   private long calcSumRemote(@Nonnull DRPCClient client, String jobId,
         int from, int to, @Min(1) int step,
         @Min(1) int partitions, @Min(0) int delay) throws Exception{

      SumJobRequest req = new SumJobRequest(jobId, from, to, step, partitions, delay);
      String arg = mapper.writeValueAsString(req);
      logger.info("Sending job request to DRPC server: {}", arg);
      long startAt = System.currentTimeMillis();
      String resultStr = client.execute(Function.SUM.name(), arg);
      long endAt = System.currentTimeMillis();
      logger.info("Recevied job result from DRPC server: {}", resultStr);
      logger.info("Duration : {}ms for job: {}", (endAt - startAt), jobId);
      SumJobResult result = mapper.readValue(resultStr, SumJobResult.class);
      return result.getSum();
   }

   private long calcSumSimple(int from, int to, int step, int delay){

      LazyCalc calc = new LazyCalc(delay);
      long startAt = System.currentTimeMillis();
      long sum = calc.sumIntBetween(from, to, step, false);
      long endAt = System.currentTimeMillis();
      logger.info("Duration : {}ms for single thread summation: {} to {}, step: {}",
            (endAt - startAt), from, to, step);

      return sum;
   }

}
