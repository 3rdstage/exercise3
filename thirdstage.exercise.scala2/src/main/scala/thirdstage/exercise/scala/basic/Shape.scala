package thirdstage.exercise.scala.basic

abstract class Shape {
  
  def getArea(): Int
  
}

class Circle(r: Int) extends Shape {
  def getArea(): Int = { r * r * 3 }
}