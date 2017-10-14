package thirdstage.exercise.storm.case4;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import twitter4j.Status;
import twitter4j.URLEntity;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @author 3rdstage
 * @see https://bitbucket.org/qanderson/tfidf-topology/src/d8b4b5fca84b2e9a09c797334b281419ef5726b1/src/jvm/storm/cookbook/tfidf/bolt/PublishURLBolt.java?at=master
 */
public class PublishUrlBolt extends BaseRichBolt {

	Jedis jedis;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.jedis = new Jedis("localhost");
	}

	@Override
	public void execute(Tuple input) {
		Status ret = (Status) input.getValue(0);
		URLEntity[] urls = ret.getURLEntities();
		for(URLEntity url : urls){
			jedis.rpush("url", url.getURL().trim());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
