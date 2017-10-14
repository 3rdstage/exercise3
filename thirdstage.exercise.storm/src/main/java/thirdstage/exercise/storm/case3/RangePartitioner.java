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

public class RangePartitioner extends BaseFunction {

	@Override
	public void execute(TridentTuple input, TridentCollector collector){
		int from = input.getInteger(0);
		int to = input.getInteger(1);
		if(from > to){
			int temp = from;
			from = to;
			to = temp;
		}

		for(int i = from; i <= to; i = i + 10){
			collector.emit(new Values(i, Math.min(to, i + 9)));
		}
	}
}
