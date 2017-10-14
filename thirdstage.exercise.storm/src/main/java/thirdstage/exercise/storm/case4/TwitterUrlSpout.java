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

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TwitterUrlSpout extends BaseRichSpout {

	private Jedis jedis;
	private SpoutOutputCollector collector;

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		String host = conf.get(Conf.REDIS_HOST_KEY).toString();
		int port = Integer.valueOf(conf.get(Conf.REDIS_PORT_KEY).toString());
		this.collector = collector;

		this.jedis = new Jedis(host, port);
	}

	@Override
	public void nextTuple() {
		String url = jedis.rpop("url");
		if(url == null){
			try{ Thread.sleep(100); }
			catch(InterruptedException ex){}
		}else{
			collector.emit(new Values(url));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("url"));

	}

}
