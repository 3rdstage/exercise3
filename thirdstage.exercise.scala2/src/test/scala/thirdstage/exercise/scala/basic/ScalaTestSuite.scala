package thirdstage.exercise.scala.basic

import org.scalatest._

class ScalaTestSuite extends FunSuite with BeforeAndAfter {

  var counter = 0

  before{
    this.counter = 1
  }


  test("1 + 1 should be 2"){
    var result = this.counter + 1;
    assert(result == 2, "1 + 1 is 2")
    System.out.println("This is : " + this.hashCode)

  }

  test("1 - 1 should be 0"){
    var result = this.counter - 1;
    assert(result == 0, "1 - 1 is 0")
    System.out.println("This is : " + this.hashCode)
  }

  test("1 * 10 should be 10"){
    var result = this.counter * 10;
    assert(result == 10, "1 x 10 is 10")
    System.out.println("This is : " + this.hashCode)
  }

}