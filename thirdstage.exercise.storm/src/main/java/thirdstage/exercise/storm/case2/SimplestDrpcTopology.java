package thirdstage.exercise.storm.case2;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @author 3rdstage
 * @see https://storm.apache.org/documentation/Distributed-RPC.html
 */
public class SimplestDrpcTopology {

	protected static final String topologyName =  "exclamation";

	public static void main(String[] args) throws Exception{

		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder(topologyName);
		builder.addBolt(new ExclaimBolt(), 3);
		Config config = new Config();
		config.setDebug(true);

		if(args == null || args.length == 0){
			LocalDRPC drpc = new LocalDRPC();
			LocalCluster cluster = new LocalCluster();


			cluster.submitTopology("drpc-sample", config, builder.createLocalTopology(drpc));
			System.err.println("Result for 'Hello' : " + drpc.execute(topologyName, "Hello"));
			System.err.println("Result for 'Hi ~' : " + drpc.execute(topologyName, "Hi ~"));

			Thread.sleep(1000);
			cluster.shutdown();
			drpc.shutdown();
		}
		else{
			config.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createRemoteTopology());
		}

	}


}
