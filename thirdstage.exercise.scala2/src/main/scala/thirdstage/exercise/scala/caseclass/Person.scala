package thirdstage.exercise.scala.caseclass

case class Person(var name: String, var bornIn: Integer, var phone: String, var email: String)

object Person{
  
  def apply(name: String, bornIn: Integer){
    apply(name, bornIn, "", "")
  }
  
  def apply(name: String, bornIn: Integer, phone: String){
    apply(name, bornIn, phone, "")
  }
  
  
}
