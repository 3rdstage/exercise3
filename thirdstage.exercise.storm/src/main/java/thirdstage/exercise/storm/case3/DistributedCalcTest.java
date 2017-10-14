package thirdstage.exercise.storm.case3;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DistributedCalcTest {


	public static void main(String[] args) throws Exception{

		System.setProperty("java.net.preferIPv4Stack", "true");

		TridentTopology topology = new TridentTopology();
		LocalDRPC drpc = new LocalDRPC();

		topology.newDRPCStream("summation", drpc)
			.each(new Fields("args"), new RangeParser(), new Fields("from", "to"))
			.each(new Fields("from", "to"), new RangePartitioner(), new Fields("sub-from", "sub-to"))
			.each(new Fields("sub-from", "sub-to"),  new RangeSummation(), new Fields("subsum"));

		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("summation", conf, topology.build());

		Thread.sleep(2000);

		drpc.execute("summation", "1,100");
	}

}
