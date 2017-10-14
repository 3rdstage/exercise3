package thirdstage.exercise.storm.case1;

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
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * @author 3rdstage
 * @see https://github.com/apache/storm/blob/master/examples/storm-starter/src/jvm/storm/starter/WordCountTopology.java
 */
public class WordCountTopology {

	public static void main(String[] args) throws Exception{
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("sentences", new RandomSentenceSpout(), 5);

		builder.setBolt("words", new SplitSentence(), 8).shuffleGrouping("sentences");
		builder.setBolt("counts", new WordCount(), 12).fieldsGrouping("words", new Fields("word"));

		Config config = new Config();
		config.setDebug(true);

		if(args != null && args.length > 0){
			config.setNumWorkers(3);

			StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createTopology());
		}
		else{
			config.setMaxTaskParallelism(3);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word-count", config, builder.createTopology());

			Thread.sleep(10000);

			cluster.shutdown();
		}

	}

}
