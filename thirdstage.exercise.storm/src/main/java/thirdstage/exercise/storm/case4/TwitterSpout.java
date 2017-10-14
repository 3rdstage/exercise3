package thirdstage.exercise.storm.case4;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @author 3rdstage
 * @see https://bitbucket.org/qanderson/tfidf-topology/src/d8b4b5fca84b2e9a09c797334b281419ef5726b1/src/jvm/storm/cookbook/tfidf/spout/TwitterSpout.java?at=master
 */
@NotThreadSafe
public class TwitterSpout extends BaseRichSpout {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private LinkedBlockingQueue<Status> queue;

	private TwitterStream twitterStream;
	private String[] trackTerms;
	private long maxQueueDepth;
	private SpoutOutputCollector collector;

	public TwitterSpout(String[] trackTerms, long maxQueueDepth){
		this.trackTerms = trackTerms;
		this.maxQueueDepth = maxQueueDepth;
	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		queue = new LinkedBlockingQueue<Status>(1000);
		StatusListener listener = new StatusListener(){
			@Override
			public void onException(Exception ex) {}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {}

			@Override
			public void onStallWarning(StallWarning warning) {
				logger.error("Stall warning received : " + warning);
			}

			@Override
			public void onStatus(Status status){
				if(queue.size() < maxQueueDepth){
					logger.trace("TWEET Received : " + status);
					queue.offer(status);
				}else{
					logger.error("Queue is now FULL. The following message is dropped : " + status);
				}
			}
		};

		this.twitterStream = (new TwitterStreamFactory()).getInstance();
		this.twitterStream.addListener(listener);
		FilterQuery filter = new FilterQuery();
		filter.count(0);
		filter.track(this.trackTerms);
		this.twitterStream.filter(filter);

	}

	@Override
	public void nextTuple() {
		Status ret = queue.poll();
		if(ret == null){
			try{ Thread.sleep(100); }
			catch(InterruptedException ex){}
		}else{
			this.collector.emit(new Values(ret));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));

	}

}
