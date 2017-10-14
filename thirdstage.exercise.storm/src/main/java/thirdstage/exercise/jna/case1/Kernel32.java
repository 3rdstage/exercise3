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

import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public interface Kernel32 extends StdCallLibrary {

	public static class SYSTEMTIME extends Structure{

		public short wYear;
		public short wMonth;
		public short wDayOfWeek;
		public short wDay;
		public short wHour;
		public short wMinute;
		public short wSecond;
		public short wMilliseconds;

		public List getFieldOrder(){
			List names = new ArrayList(8);
			names.add("wYear");
			names.add("wMonth");
			names.add("wDayOfWeek");
			names.add("wDay");
			names.add("wHour");
			names.add("wMinute");
			names.add("wSecond");
			names.add("wMilliseconds");
			return names;
		}
	}

	void GetLocalTime(SYSTEMTIME result);

}
