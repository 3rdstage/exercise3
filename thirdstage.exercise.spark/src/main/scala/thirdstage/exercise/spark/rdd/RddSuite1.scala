package thirdstage.exercise.spark.rdd

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.apache.spark.SparkContext
import com.holdenkarau.spark.testing.SharedSparkContext

class RddSuite1 extends FunSuite with SharedSparkContext{


  test("The simplest transformation"){
    val input = List("hi", "hi cloudera", "bye")
    val expected = List(List("hi"), List("hi", "cloudera"), List("bye"))

  }

}