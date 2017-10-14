package thirdstage.exercise.jna.case1;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Native;

import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FirstExercise {


	public static void main(String[] args){

		 Kernel32 lib = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class);
		 Kernel32.SYSTEMTIME time = new Kernel32.SYSTEMTIME();
		 lib.GetLocalTime(time);

		 System.out.printf("Today is %1$4d-%2$02d-%3$02d.\n", time.wYear, time.wMonth, time.wDay);
		 System.out.printf("And current time is %1$02d:%2$02d:%3$02d", time.wHour, time.wMinute, time.wSecond);
	}

}
