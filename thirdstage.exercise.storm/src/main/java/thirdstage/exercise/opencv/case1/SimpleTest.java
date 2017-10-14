package thirdstage.exercise.opencv.case1;

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
import org.opencv.core.MatOfRect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SimpleTest {

	public static void main(String[] args) throws Exception{
		SimpleTest testee = new SimpleTest();

		testee.run();

	}

	public void run() throws Exception{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		URL classifierUrl = ClassLoader.getSystemResource("thirdstage/exercise/opencv/case1/lbpcascade_frontalface.xml");
		String classifierPath = (new java.io.File(classifierUrl.toURI())).getAbsolutePath();
		System.out.printf("The full path for lbpcascade_frontalface.xml is : %1$s\n", classifierPath);
		CascadeClassifier faceDetector = new CascadeClassifier(classifierPath);

		URL imgUrl = ClassLoader.getSystemResource("thirdstage/exercise/opencv/case1/AverageMaleFace.jpg");
		String imgPath = (new java.io.File(imgUrl.toURI())).getAbsolutePath();
		Mat img = Highgui.imread(imgPath);

		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDetections);

		System.out.printf("Detected %1$s faces with the image of %2$s", faceDetections.toArray().length, imgPath);
	}

}
