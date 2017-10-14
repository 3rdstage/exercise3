package thirdstage.exercise.opencv.case1;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class VideoProcessingTest {

	public static void main(String[] args) throws Exception{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


		URL vUrl = ClassLoader.getSystemResource("thirdstage/exercise/opencv/case1/drop.avi");
		String vPath = (new java.io.File(vUrl.toURI())).getAbsolutePath();
		VideoCapture vc = new VideoCapture(vPath);

		if(!vc.isOpened()){
			System.out.println("Error");
			System.exit(1);
		}else{
			System.out.printf("Successfully read video file : %s\n", vPath);
		}

		Mat frame = new Mat();
		int cnt = 1;
		while(vc.read(frame)){
			cnt++;
			if(cnt == 10){
				Highgui.imwrite(System.getenv("TEMP") + File.separator + "d:/temp/drop-f10.png", frame);

			}


		}
		System.out.printf("Finished to read all frames. Total number of frames : %1$d ", cnt);

	}

}
