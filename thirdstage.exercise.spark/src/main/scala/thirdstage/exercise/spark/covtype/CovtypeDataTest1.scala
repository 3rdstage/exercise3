package thirdstage.exercise.spark.covtype

import sys.process._
import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite
import java.io.File
import java.net.URL
import org.slf4j.LoggerFactory
import org.apache.commons.io.FileUtils
import org.zeroturnaround.zip.ZipUtil
import java.util.zip.GZIPInputStream
import java.io.FileInputStream
import java.io.BufferedInputStream
import java.io.FileOutputStream

/**
 * This class follows the example of chapter 4 'Predicting Forest Cover with Decision Trees'
 * of the book 'Advanced Analytics with Spark' by Sandy Ryza and et. al. at 2015
 *
 * The sample data for this example can be downloaded at 'https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/'
 * The scheme of the data is explained at 'https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/covtype.info'
 *
 * @see https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/
 * @see https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/covtype.info
 */
class CovtypeDataTest1 extends FunSuite with SharedSparkContext {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private var workDir  = ""
  private var filePath = "" //full path for the data file

  override def beforeAll {
    super.beforeAll()

    this.workDir = System.getProperty("workDir")
    if (workDir == null || workDir.isEmpty()) {
      throw new IllegalStateException("The 'workDir' should be provided at command-line")
    }

    this.filePath = workDir + raw"\covtype.data"
    val dFile = new File(filePath)
    val zfPath = workDir + raw"\covtype.data.gz"  //Need to enhance the code in case when 'workDir' ends with path separator (slash or back-slash)
    val zFile = new File(zfPath)
    val url = "https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/covtype.data.gz"

    if(!dFile.exists()){
      if(!zFile.exists()){
        FileUtils.forceMkdir(new File(workDir))
        logger.info("The archive file {} containing sample data file dosen't exist. Now the file will be downloaded from {}.", zfPath, url:Any)
        logger.info("Downloading {}", url:Any)
        new URL(url) #> zFile !!

        logger.info("Completed downloading {}.", url:Any)
      }

      logger.info("Start to unzip archive file {} to {}",  zfPath, workDir:Any)
      ZipUtil.unpack(zFile, new java.io.File(workDir))
      logger.info("Completed unzipping archive file to {}", filePath)

    }else{
      logger.info("Sample data file is already prepared at {}", filePath)
    }


  }

  private def unzip(zfPath:String, tfPath:String) = {
    val in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(zfPath)))
    val out = new FileOutputStream(tfPath)

  }


  test("Dummy test - test nothing but just assert true."){
    assert(true);
  }

  test("Raw data contains 581012 rows."){
    val raw = sc.textFile(this.filePath)

    assert(raw.count() == 581012)

  }

}