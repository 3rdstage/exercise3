package thirdstage.exercise.spark.translate

import org.apache.spark.launcher.SparkLauncher
import com.google.common.collect.Maps
import java.util.HashMap

object TranslateAppLocalLauncher extends App {


  override def main(args: Array[String]) {

    val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)
    logger.info("Try to launch Spark transalation application")

    val env = new HashMap[String, String]()
    env.put("SPARK_PRINT_LAUNCH_COMMAND", "1");

    val masterUrl = "spark://localhost:" + (if(System.getenv("SPARK_MASTER_PORT") == null) "7077" else System.getenv("SPARK_MASTER_PORT"))

    val spark = new SparkLauncher(env)
      .setAppResource(System.getProperty("appl.spark.resource"))
      .setMainClass("thirdstage.exercise.spark.translate.TranslateApp")
      .setMaster(masterUrl)
      .setDeployMode("cluster")
      .addSparkArg("--verbose")
      .setVerbose(true)

    //val process = spark.launch()
    spark.startApplication()

    logger.info("Launched Spark translation application")

    System.out.println("Press [Enter] key to end this process.");

    var cnt = 0
    var keyed = false
    do {
      cnt = System.in.available()
      if (cnt >= 1) keyed = true
      else Thread.sleep(500)
    } while (!keyed)

    while (cnt > 0) {
      System.in.read()
      cnt -= cnt
    }
  }
}