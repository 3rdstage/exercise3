package thirdstage.exercise.storm.calc;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SumJobBoltTest {


   @Test
   public void testPartitionSumRequests(){
      SumJobRequest req = new SumJobRequest("R001", 0, 10, 2, 3, SumJobRequest.DEFAULT_DELAY);
      List<SumTaskRequest> taskReqs = SumJobBolt.partitionSumRequest(req);

      Assert.assertEquals(taskReqs.size(), req.getPartitions());
      Assert.assertEquals(taskReqs.get(0).getFrom(), req.getFrom());
      Assert.assertEquals(taskReqs.get(0).getTo(), 2);
      Assert.assertEquals(taskReqs.get(1).getFrom(), 4);
      Assert.assertEquals(taskReqs.get(1).getTo(), 6);
      Assert.assertEquals(taskReqs.get(2).getFrom(), 8);
      Assert.assertEquals(taskReqs.get(2).getTo(), req.getTo());
   }

   @Test
   public void testPartitionSumRequests2(){
      SumJobRequest req = new SumJobRequest("R002", 1, 10, 2, 3, 100);
      List<SumTaskRequest> taskReqs = SumJobBolt.partitionSumRequest(req);

      Assert.assertEquals(taskReqs.size(), req.getPartitions());
      Assert.assertEquals(taskReqs.get(0).getFrom(), req.getFrom());
      Assert.assertEquals(taskReqs.get(0).getTo(), 3);
      Assert.assertEquals(taskReqs.get(1).getFrom(), 5);
      Assert.assertEquals(taskReqs.get(1).getTo(), 7);
      Assert.assertEquals(taskReqs.get(2).getFrom(), 9);
      Assert.assertEquals(taskReqs.get(2).getTo(), req.getTo());
   }

   @Test
   public void testPartitionSumRequests3(){
      SumJobRequest req = new SumJobRequest("R003", 1, 10, 2, 3, 200);
      List<SumTaskRequest> taskReqs = SumJobBolt.partitionSumRequest(req);

      Assert.assertEquals(taskReqs.size(), req.getPartitions());
      Assert.assertEquals(taskReqs.get(0).getFrom(), req.getFrom());
      Assert.assertEquals(taskReqs.get(0).getTo(), 3);
      Assert.assertEquals(taskReqs.get(1).getFrom(), 5);
      Assert.assertEquals(taskReqs.get(1).getTo(), 7);
      Assert.assertEquals(taskReqs.get(2).getFrom(), 9);
      Assert.assertEquals(taskReqs.get(2).getTo(), req.getTo());
   }

   @Test
   public void testPartitionSumRequests4(){
      SumJobRequest req = new SumJobRequest("R004", 1, 10, 2, 4, 3000);
      List<SumTaskRequest> taskReqs = SumJobBolt.partitionSumRequest(req);

      Assert.assertEquals(taskReqs.size(), req.getPartitions());
      Assert.assertEquals(taskReqs.get(0).getFrom(), req.getFrom());
      Assert.assertEquals(taskReqs.get(0).getTo(), 3);
      Assert.assertEquals(taskReqs.get(1).getFrom(), 5);
      Assert.assertEquals(taskReqs.get(1).getTo(), 5);
      Assert.assertEquals(taskReqs.get(2).getFrom(), 7);
      Assert.assertEquals(taskReqs.get(2).getTo(), 7);
      Assert.assertEquals(taskReqs.get(3).getFrom(), 9);
      Assert.assertEquals(taskReqs.get(3).getTo(), req.getTo());
   }

   @Test
   public void testPartitionSumRequests5(){
      SumJobRequest req = new SumJobRequest("R005", 1, 100, 1, 4, 100000);
      List<SumTaskRequest> taskReqs = SumJobBolt.partitionSumRequest(req);

      LazyCalc calc = new LazyCalc(0);
      long sum = 0;

      for(SumTaskRequest taskReq: taskReqs){
         sum = sum + calc.sumIntBetween(taskReq.getFrom(), taskReq.getTo(), taskReq.getStep());
      }

      long sum0 = calc.sumIntBetween(req.getFrom(), req.getTo(), req.getStep());

      Assert.assertEquals(sum, sum0);
      Assert.assertEquals(sum, 5050);

   }

}
