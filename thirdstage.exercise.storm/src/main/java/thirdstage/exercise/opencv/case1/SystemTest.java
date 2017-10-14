package thirdstage.exercise.opencv.case1;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SystemTest {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test(description="Test load a native library multiple times.")
	public void testLoadLibrary() throws Exception{

		int cnt = 3;

		for(int i = 0; i < cnt; i++){
			try{
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

				this.logger.info("Loaded native library: {}, times: {}", Core.NATIVE_LIBRARY_NAME, i);
			}catch(Exception ex){
				this.logger.error("Fail to load native library: " + Core.NATIVE_LIBRARY_NAME, ex);
				throw ex;
			}
		}
		
		Mat mat = new Mat(); 
		
		
	}

}
