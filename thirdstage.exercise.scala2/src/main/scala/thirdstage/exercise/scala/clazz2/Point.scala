package thirdstage.exercise.scala.clazz2

class Point (var x: Int, var y: Int) {
  
  def move(dx: Int, dy: Int){
    x = x + dx
    y = y + dy
  }
  
}