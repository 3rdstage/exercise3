package thirdstage.exercise.jni.case1;

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

public class HelloWorldUsingJni {

	private native void print();

	static{

		//System.loadLibrary("HolloWorld");
	}

	public static void main(String[] args){
		new HelloWorldUsingJni().print();

	}

}
