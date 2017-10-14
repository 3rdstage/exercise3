package thirdstage.exercise.scala.clazz2

import org.scalatest.FunSuite

class PointSuite extends FunSuite{
  
  test("var parameters in class constructor"){
    
    val p = new Point(3, 4)
    
    this.assertResult(3)(p.x)
  }
  
  
  
}