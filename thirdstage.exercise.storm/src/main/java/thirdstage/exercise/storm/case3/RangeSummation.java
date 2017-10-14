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

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class RangeSummation extends BaseFunction {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		int from = tuple.getInteger(0);
		int to = tuple.getInteger(1);

		logger.info("Catch the range to sum up - from : {}, to : {}", from, to);

		collector.emit(new Values(-1));
	}

}
