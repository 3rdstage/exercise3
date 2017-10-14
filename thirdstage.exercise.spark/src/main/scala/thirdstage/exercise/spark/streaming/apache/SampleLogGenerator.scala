package thirdstage.exercise.spark.streaming.apache

import java.io.FileReader
import java.net.URL

import scala.sys.process._

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.time.FastDateFormat
import org.slf4j.LoggerFactory
import org.zeroturnaround.zip.ZipUtil

/**
 * This application would create access log files (names of 'access_yyyyMMddHHmmssSSS.log') and
 * error log file (names of 'error_yyyyMMddHHmmssSSS.log') under the directory of '${workDir}/logs'
 * where the 'workDir' is specified via Java system properties.
 * <p>
 * This application expects single parameter at runtime which is to be 'access_log' or 'error_log'
 *
 * @author Sangmoon Oh
 * @since 2016-10-11
 */
object SampleLogGenerator {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]) = {

    val workDir = System.getProperty("workDir")
    if (workDir == null || workDir.isEmpty()) {
      throw new IllegalStateException("The 'workDir' should be provided at command-line")
    }

    val logLines = Integer.valueOf(System.getProperty("logLines", "100"))
    if(logLines < 100){
      throw new IllegalStateException("The 'logLines' should be at least 100")
    }

    val sleepMilliSec = Integer.valueOf(System.getProperty("sleepMilliSec", "200"))
    if(sleepMilliSec < 100){
      throw new IllegalStateException("The 'sleepMilliSec' should be at least 100")
    }

    val dPath = workDir + raw"\error_log"
    val dFile = new java.io.File(dPath)
    val zfPath = workDir + raw"\apache-samples.rar"  //Need to enhance the code in case when 'workDir' ends with path separator (slash or back-slash)
    val zFile = new java.io.File(zfPath)
    val url = "http://www.monitorware.com/en/logsamples/download/apache-samples.rar"

    if(!dFile.exists()){
      if(!zFile.exists()){
        FileUtils.forceMkdir(new java.io.File(workDir))
        logger.info("The archive file {} containing sample data file dosen't exist. Now the file will be downloaded from {}.", zfPath, url:Any)
        logger.info("Downloading {}", url:Any)
        new URL(url) #> zFile !!

        logger.info("Completed downloading {}.", url:Any)
      }

      logger.info("Start to unzip archive file {} to {}",  zfPath, workDir:Any)
      ZipUtil.unpack(zFile, new java.io.File(workDir))
      logger.info(raw"Completed unzipping archive file to {}\error_log and {}\access_log", workDir)
    }else{
      logger.info(raw"Sample data files ({}\error_log and {}\access_log) are already prepared at {}", workDir, workDir, workDir)
    }

    var in :java.io.BufferedReader = null
    var out :java.io.FileOutputStream = null
    try{
      if(!(args.length == 1 && (Array("access_log", "error_log") contains args(0)))){
        System.out.println("Usage - java SampleLogGenerator access_log|error_log")
        System.exit(0)
      }

      val ft = args(0).substring(0, args(0).indexOf('_')) //output file type : 'access' or 'error'

      def timestamp() = {
        val formatter = FastDateFormat.getInstance("yyyyMMddHHmmssSSS")
        formatter.format(new java.util.Date())
      }

      FileUtils.forceMkdir(new java.io.File(workDir + "/logs"))
      var fn = workDir + "/" + ft + "_" + timestamp() + ".log"

      in = new java.io.BufferedReader(new FileReader(new java.io.File(workDir + "/" + args(0))))
      out = new java.io.FileOutputStream(new java.io.File(fn))
      var line:String = null
      var cnt = 0
      var fn2:String = null
      while(true){
        line = in.readLine()
        if(line == null){
          in.close()
          in = new java.io.BufferedReader(new FileReader(new java.io.File(workDir + "/" + args(0))))
          line = in.readLine()
        }
        out.write((line + "\n").getBytes())
        out.flush()
        logger.debug("Wrote a line to {}", fn)
        cnt += 1
        if(cnt >= logLines){
          out.close()
          //move the closed file
          fn2 = workDir + "/logs/" + fn.substring(fn.lastIndexOf("/") + 1)
          new java.io.File(fn).renameTo(new java.io.File(fn2))
          logger.debug("Move the log file {} under 'logs' directory", fn)

          //next output file
          fn = workDir + "/" + ft + "_" + timestamp() + ".log"
          out = new java.io.FileOutputStream(new java.io.File(fn))
          cnt = 0
        }
        Thread.sleep(sleepMilliSec.toLong)
      }

    }catch {
      case ex:Exception => ex.printStackTrace(System.err)
    }finally{

      if(out != null){
        try{ out.close() }catch{ case ex:Exception => }
      }
      if(in != null){
        try{ in.close() }catch{ case ex:Exception => }
      }
    }
  }

}