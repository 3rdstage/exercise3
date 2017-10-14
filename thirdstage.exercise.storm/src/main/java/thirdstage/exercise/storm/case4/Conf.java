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
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class Conf {

	public static final String REDIS_HOST_KEY = "redisHost";
	public static final String REDIS_PORT_KEY = "redisPort";
	public static final String DEFAULT_JEDIS_PORT = "6379";

}
