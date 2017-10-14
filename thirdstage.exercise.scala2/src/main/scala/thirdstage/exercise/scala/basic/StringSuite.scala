package thirdstage.exercise.scala.basic

import org.scalatest.FunSuite

class StringSuite extends FunSuite{


  test("stripMargin removes the leading margins of multi-line string"){
    val str = """|Today
                 |Tomorrow""".stripMargin

    val n = "Today".length() + 1 + "Tomorrow".length()
    this.assertResult(n)(str.length())
  }


}