package thirdstage.exercise.spark.streaming.apache

import java.util.regex.Pattern
import java.util.regex.Matcher
import org.slf4j.LoggerFactory

class LogAnalyzer extends Serializable{

  private val logger = LoggerFactory.getLogger(this.getClass)

  def transformLogData(line : String) : Map[String, String] = {

    val pattern = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+) (\S+)" (\d{3}) (\S+)"""
    val matcher = Pattern.compile(pattern).matcher(line)


    if(!matcher.find()){
      logger.warn("Cannot parse the specified string : {}", line)
      createEmptyDataMap()
    }else{
      createDataMap(matcher)
    }
  }

  private def createDataMap(m:Matcher) = {
    Map[String, String](("IP" -> m.group(1)),
        ("client" -> m.group(2)),
        ("user" -> m.group(3)),
        ("date" -> m.group(4)),
        ("method" -> m.group(5)),
        ("request" -> m.group(6)),
        ("protocol" -> m.group(7)),
        ("respCode" -> m.group(8)),
        ("size" -> m.group(9)))
  }

  private def createEmptyDataMap() = {
    Map[String, String](("IP" -> ""),
        ("client" -> ""),
        ("user" -> ""),
        ("date" -> ""),
        ("method" -> ""),
        ("request" -> ""),
        ("protocol" -> ""),
        ("respCode" -> ""),
        ("size" -> ""))
  }


}