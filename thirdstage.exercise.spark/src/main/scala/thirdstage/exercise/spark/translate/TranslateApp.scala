package thirdstage.exercise.spark.translate

import org.slf4j.LoggerFactory
import scala.io.Source
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext


object TranslateApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val dictionaries = scala.collection.mutable.Map[String, Map[String, String]]()

  private val supportedLangs = Set("German", "French", "Italian", "Spanish")

  def main(args: Array[String]){

    System.out.println("Starting Spark Application")

    if(args.length != 4){
      logger.warn("Fail to execute application due to improper number of argument")
      System.err.println("Usage : TranslateApp <app_name> <book_path> <output_path> <lang>")
      System.exit(1)
    }

    val Seq(appName, bookPath, outputPath, lang) = args.toSeq
    val dict = getDictionary(lang)

    val conf = new SparkConf().setAppName(appName).setJars(SparkContext.jarOfClass(this.getClass).toSeq)
    val sc = new SparkContext(conf)
    sc.setLogLevel("TRACE")
    val book = sc.textFile(bookPath)
    val translated = book.map(line => line.split("\\s+").map(word => dict.getOrElse(word, word)).mkString(" "))
    translated.saveAsTextFile(outputPath)
  }

  def getDictionary(lang: String): Map[String, String] = {

    if(!this.supportedLangs.contains(lang)){
      System.err.println("Unsupported language: %s".format(lang))
      System.exit(1)
    }

    if(this.dictionaries.contains(lang)){
      logger.info("The cache contains the %s dictionary".format(lang))
      return this.dictionaries.get(lang).get
    }

    val url = "http://www.june29.com/IDP/files/%s.txt".format(lang)
    logger.info("Grabbing dictionary from : %s".format(url))
    val map = Source.fromURL(url, "ISO-8859-1").mkString
      .split("\\r?\\n")
      .filter(line => !line.startsWith("#"))
      .map(line => line.split("\\t"))
      .map(tkns => (tkns(0).trim, tkns(1).trim)).toMap

    logger.info("Parsed %s dictionary containing %d words".format(lang, map.size))

    this.dictionaries(lang) = map
    return map
  }


}