package thirdstage.exercise.opencv.case1;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class EclipseSetupTest {

	public static void main(String[] args){

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.printf("The version of OpenCV.Core : %1$s\n", Core.VERSION);

		Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		System.out.println("mat = " + mat.dump());

	}

}
