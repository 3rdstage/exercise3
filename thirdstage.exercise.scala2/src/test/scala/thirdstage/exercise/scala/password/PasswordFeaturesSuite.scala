package thirdstage.exercise.scala.password

import org.scalatest.FunSuite

class PasswordFeaturesSuite extends FunSuite{
  
  test("Creating PasswordFeatures object just with min length"){
    
    val features = PasswordFeatures(5)
    
    assert(features.getMinLength == 5)
    assert(features.getMaxLength == PasswordFeatures.LengthMaxDefault, 
        "The features has default for max length.")
  }
  
}