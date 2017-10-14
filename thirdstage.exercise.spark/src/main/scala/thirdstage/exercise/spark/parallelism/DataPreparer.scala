package thirdstage.exercise.spark.parallelism

import scala.sys.process._
import org.zeroturnaround.zip.ZipUtil
import org.apache.commons.io.FileUtils

object DataPreparer extends App {
  
  private def logger = org.slf4j.LoggerFactory.getLogger(this.getClass)
  
  override def main(agrs:Array[String]){

    val workDir = System.getProperty("workDir")
    if (workDir == null || workDir.isEmpty()) {
      throw new IllegalStateException("The 'workDir' should be provided at command-line")
    }
    
    FileUtils.forceMkdir(new java.io.File(workDir))

    val urlBase = "http://stat-computing.org/dataexpo/2009/"
    val names = Array("1987.csv", "1988.csv")
    
    for(name <- names){
      var path = workDir + "\\" + name //unzipped file
      var file = new java.io.File(path)
      
      if(!file.exists()){
        var path2 = path + ".bz2" //zipped file
        var file2 = new java.io.File(path2)
        if(!file2.exists()){
          var url = urlBase + name + ".bz2"
          logger.info("The archive file {} containing sample data file dosen't exist. Now the file will be downloaded from {}.", path2:Any, url)
          new java.net.URL(url) #> file2 !!
        }
        
        logger.info("Start to unzip archive file {} to {}", path2, workDir:Any)
        ZipUtil.unpack(file2, new java.io.File(workDir))
        logger.info(raw"Completed unzipping archive file to ", path)
        
      }else{
        logger.info("Sample data file {} is already prepared at {}.", path)
      }

    }  
    
  }
  
}