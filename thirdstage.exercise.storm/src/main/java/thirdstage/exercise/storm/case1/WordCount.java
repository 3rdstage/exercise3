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
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


public class WordCount extends BaseBasicBolt{

	Map<String, Integer> counts = new HashMap<String, Integer>();

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String word = tuple.getString(0);
		Integer count = counts.get(word);
		if(count == null) count = 0;
		count++;
		counts.put(word, count);
		collector.emit(new Values(word, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("word", "count"));
	}
}
