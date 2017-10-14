package thirdstage.exercise.spark.parallelism

import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import java.util.ArrayList
import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD

/**
 * This sample application are from the article <a href="http://www.bigsynapse.com/spark-input-output">"Controlling Parallelism in Spark"</a>
 * 
 * 
 * @author Sangmoon Oh
 * 
 * @see http://www.bigsynapse.com/spark-input-output
 * @see https://github.com/sameeraxiomine/sparkusingjava8/blob/master/src/main/java/com/axiomine/spark/examples/io/PrintSparkMapSidePartitionSizeControl.java
 * @see https://github.com/sameeraxiomine/sparkusingjava8/blob/master/src/main/java/com/axiomine/spark/examples/io/SparkMapSidePartitionSizeControl.java
 */
object PartitionSizeControl {
  
  val OneMB = 1024*1024
  
  private val logger = LoggerFactory.getLogger(this.getClass)
  
  
  def main(args:Array[String]) = {
    
    val inputPath = args(0)
    var desc = "Scenario 1. Default"
    var partitions = 0
    var blockSize = 0L  //32MB by default
    var minBlockSz = 0L  //default
    
  }
  
  def print(desc:String, inputPath:String, partitions:Int, blockSz:Long, minPartitionSz:Long) = {
    
    val blockSize = if(blockSz == 0) 32 * OneMB else blockSz

    logger.info("********** " + desc + " **********")
    val goalSize = getGoalSize(inputPath, partitions)
    //val partitionSize = 
    
  }
  
  def getGoalSize(inputPath:String, partitions:Int) = {
    val fs = FileSystem.get(new Configuration())
    val files = fs.listFiles(new Path(inputPath), true)
    
    var totalSize = 0L
    while(files.hasNext()){
      val f = files.next()
      if(!f.isDirectory()) totalSize += f.getLen()
    }

    Math.round(totalSize.toFloat/(if(partitions == 0) 1 else partitions))
    
  }
  
  def computePartitionSize(goalSz:Long, minPartitionSz:Long, blockSz:Long) = {
    val blockSize = if(blockSz == 0) 32 * OneMB else blockSz
    
    val minPartitionSize = if(minPartitionSz == 0) 1 else minPartitionSz
    
    Math.max(minPartitionSize, Math.min(goalSz, blockSize))
  }
  
  
  def printSizeOfEachPartition(inputPath:String, partSize:Long, splitsOverall:Boolean) = {
  
    val fs = FileSystem.get(new Configuration())
    val files = fs.listFiles(new Path(inputPath), true)
 
    val partitions = new ArrayList[Long]()
    
    while(files.hasNext()){
      val f = files.next()
      
      if(!f.isDirectory()){
        val size = f.getLen
        logger.info("Input file - name: {}, size: {}", f.getPath:Any, size)
        
        
      }
    }
    
    def controlMapSidePartitionSize(inputPath:String, outputPath:String, partitions:Int, batchSize:Long, minPartitionSize:Long) = {
      val cntx = new SparkContext("local", "PartitionSizeControl")
      
      if(batchSize > 0){
        cntx.hadoopConfiguration.setLong("fs.local.block.size", batchSize)
            
      }
      if(minPartitionSize > 0){
        cntx.hadoopConfiguration.setLong("mapreduce.input.fileinputformat.split.minsize", minPartitionSize)
        
      }
      
      FileUtils.deleteQuietly(new java.io.File(outputPath))
      val rdd:RDD[String] = if (partitions > 0) cntx.textFile(inputPath, partitions) else cntx.textFile(inputPath)
      
      logger.info("The number of partitions : {}", rdd.partitions.size) 
      
      val rdd2 = rdd.map(x => x)
      rdd2.saveAsTextFile(outputPath)
        
    }
    
  }
  
  
  
  
}