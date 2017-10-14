package thirdstage.exercise.opencv.case2;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RemapTest{
   
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @BeforeTest
   public void beforeTest() throws Exception{
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   }
   
   @Test
   public void testRemap1() throws Exception{
      
      String srcRes = "thirdstage/exercise/opencv/case2/3rdstage.jpg";
      int trgWidth = 300;
      int trgHeight = 200;
      
      
      URI srcUri = ClassLoader.getSystemResource(srcRes).toURI();
      String srcName = srcUri.getSchemeSpecificPart();
      if(srcName.startsWith("/")){
         srcName = srcName.substring(1);
      }
      
      Mat srcMat = Highgui.imread(srcName);
      Mat trgMat = new Mat(300, 300, CvType.CV_8UC3);
      
      Imgproc.resize(srcMat, trgMat, new Size(trgWidth, trgHeight));
      
      String trgName = System.getenv("TEMP") + File.separator + "opencv-test";
      FileUtils.forceMkdir(new File(trgName));
      trgName = trgName + File.separator + "3rdstage-300.jpg";

      Highgui.imwrite(trgName, trgMat);
      
   }

}
