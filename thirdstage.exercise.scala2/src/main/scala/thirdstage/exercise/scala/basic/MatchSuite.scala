package thirdstage.exercise.scala.basic

import org.scalatest.FunSuite

class MatchSuite extends FunSuite {
  
  
  
  test("Type match"){
    
    def getType(x: Any): String = x match {
      case s : String => "String"
      case i : Int => "Int"
      case l : Long => "Long"
      case f : Float => "Float"
      case d : Double => "Double"
      case l : List[_] => "List"
      case _ => "Unknown"
    }
    
    this.assertResult("String")(getType("a"))
    this.assertResult("Int")(getType(1))
    this.assertResult("Int")(getType(-100000))
    this.assertResult("Float")(getType(0.5f))
    this.assertResult("Long")(getType(10000000000L))
    this.assertResult("List")(getType(List(1, 2, 3)))
  }
  
  
  test("Switch with case object"){
    
    trait Command
    case object Begin extends Command
    case object Go extends Command
    case object Stop extends Command
    case object Oops extends Command
    
    def interpretCmd(cmd : Command) = cmd match {
      case Begin | Go => "Start"
      case Stop | Oops => "End"
    }
    
    this.assertResult("Start")(interpretCmd(Begin))
    this.assertResult("Start")(interpretCmd(Go))
    this.assertResult("End")(interpretCmd(Stop))
    this.assertResult("End")(interpretCmd(Oops))
    
  }
  
  
  
}