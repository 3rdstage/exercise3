package thirdstage.exercise.spark.streaming.apache

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite

class LogAnalyzerSuite extends FunSuite with BeforeAndAfter{


  val analyzer = new LogAnalyzer()


  test("Analyze typical single line"){

    val line = """64.242.88.10 - - [07/Mar/2004:23:47:58 -0800] "GET /twiki/bin/view/TWiki/WikiReferences?skin=print HTTP/1.1" 200 5596"""

    val data = analyzer.transformLogData(line)

    assert(data("IP") == "64.242.88.10")
    assert(data("client") == "-")
    assert(data("user") == "-")
    assert(data("date") == "07/Mar/2004:23:47:58 -0800")
    assert(data("method") == "GET")
    assert(data("request") == "/twiki/bin/view/TWiki/WikiReferences?skin=print")
    assert(data("protocol") == "HTTP/1.1")
    assert(data("respCode") == "200")
    assert(data("size") == "5596")
  }




}