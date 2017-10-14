package thirdstage.exercise.scala.generic

import thirdstage.exercise.scala.generic.RangeSpec
import org.scalatest.FunSuite
import thirdstage.exercise.scala.generic.RangeSpec

class RangeSpecSuite extends FunSuite{
  
  test("create object using apply"){
    
    val spec = RangeSpec[Int](1, 2, 3, 4)
    val v1: Int = spec.getMinLowerLimit
    
    
  }
  
}