package thirdstage.exercise.storm.case1;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;

/**
 * @author 3rdstage
 * @see https://github.com/apache/storm/blob/master/examples/storm-starter/src/jvm/storm/starter/spout/RandomSentenceSpout.java
 */
public class RandoumSentenceSpout extends BaseRichSpout {

	protected static String[] sentences = new String[]{
		"People look to me and say is the end near, when is the final day? what's the future of mankind?",
		"How do I know, I got left behind",
		"Everyone goes through changes looking to find the truth",
		"Don't look at me for answers",
		"Don't ask me",
		"I don't know",
		"How am I supposed to know hidden meanings that will never show",
		"Fools and prophets from the past",
		"Life's a stage and we're all in the cast"
	};

	SpoutOutputCollector collector;
	Random rand;



	@Override
	public void open(Map conf, TopologyContext cntx, SpoutOutputCollector collector){
		this.collector = collector;
		this.rand = new Random();
	}

	@Override
	public void nextTuple(){

	}

	@Override
	public void ack(Object id){

	}

	@Override
	public void fail(Object id){

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("word"));
	}


}
