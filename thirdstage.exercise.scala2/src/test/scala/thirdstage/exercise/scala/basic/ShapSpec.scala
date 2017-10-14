package thirdstage.exercise.scala.basic

import org.scalatest._

class ShapSpec extends FlatSpec with Matchers{
  
  "A Circle" should "have area that is 3 times of the square of its radius" in {
    val r = 10
    val circle = new Circle(r)
    
    circle.getArea() should be (300)
  }
  
}