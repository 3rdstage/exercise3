package thirdstage.exercise.spark.streaming.simple

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.rdd.RDD

object FirstStreamingApp {

  def main(args: Array[String]){

    println("Creating Spark config.")

    val conf = new SparkConf()
    conf.setAppName("First Spar Streaming Application")

    val cntx = new StreamingContext(conf, Seconds(2))

    val lines = cntx.socketTextStream("localhost", 9087, StorageLevel.MEMORY_AND_DISK_SER_2)
    val words = lines.flatMap(x => x.split(" "))
    val pairs = words.map(word => (word, 1))

    val counts = pairs.reduceByKey(_ + _)

    printValues(counts, cntx)

    cntx.start()
    cntx.awaitTermination()
  }

  def printValues(stream:DStream[(String, Int)], cntx:StreamingContext){

    stream.foreachRDD((rdd:RDD[(String, Int)]) => {
      var array = rdd.collect()
      println("----------Start Printing Result----------")
      for(item <- array){
        println(item)
      }
      println("----------Finished Printing Result----------")
    })
  }

}