package thirdstage.exercise.storm.case3;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

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

public class RangeParser extends BaseFunction {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String arg = tuple.getString(0);
		StringTokenizer st = new StringTokenizer(arg, ",");

		int from = Integer.valueOf(st.nextToken().trim());
		int to = Integer.valueOf(st.nextToken().trim());

		collector.emit(new Values(from, to));


	}

}
