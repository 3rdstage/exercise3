package thirdstage.exercise.scala.list

class ListTest extends org.scalatest.FunSuite {
  
  test("The length of concatenated list is the sum of each list"){
    val l1 = List(1, 2);
    val l2 = List(3, 4, 5);
    val l3 = l1 ::: l2;
    
    assertResult(l1.size + l2.size)(l3.size);
    
  }
  
}