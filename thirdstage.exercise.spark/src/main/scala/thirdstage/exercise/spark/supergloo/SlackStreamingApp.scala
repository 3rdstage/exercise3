package thirdstage.exercise.spark.supergloo

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds

/**
 * Spark Streaming example from 'https://www.supergloo.com/fieldnotes/spark-streaming-example-from-slack/'
 * 
 * @author 3rdstage
 * 
 * @see https://www.supergloo.com/fieldnotes/spark-streaming-example-from-slack/
 */
object SlackStreamingApp {
  
  
  def main(args: Array[String]){
    val conf = new SparkConf().setMaster(args(0)).setAppName("SlackStreaming")
    val cntx = new StreamingContext(conf, Seconds(5))
    val stream = cntx.receiverStream(new SlackReceiver(args(1)))
    
    stream.print()
    if(args.length > 2){
      stream.saveAsTextFiles(args(2))
    }
    
    cntx.start()
    cntx.awaitTermination()
    
  }
}