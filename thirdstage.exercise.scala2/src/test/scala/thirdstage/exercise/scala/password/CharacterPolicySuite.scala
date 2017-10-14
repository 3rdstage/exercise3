package thirdstage.exercise.scala.password

import org.scalatest.FunSuite

class CharacterPolicySuite extends FunSuite{
  
  test("..."){
    
    assert(CharacterPolicy.isValidSpecialCharacters(Array('!', '@', '#')))
  }
  
}