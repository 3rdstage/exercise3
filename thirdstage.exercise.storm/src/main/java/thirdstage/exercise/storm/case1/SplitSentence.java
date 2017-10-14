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

import backtype.storm.task.ShellBolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

// @UnderConstructing
public class SplitSentence extends BaseBasicBolt{


	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String sentence = tuple.getString(0);
		


	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration(){
		return null;
	}

}
