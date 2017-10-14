package thirdstage.exercise.spark.translate

class TranslateAppTest extends org.scalatest.FunSuite{
  
  test("Test getDictionary with supported language"){
    val dict = TranslateApp.getDictionary("German")
    
    assert(dict.size > 100)
    
    val dict2 = TranslateApp.getDictionary("German")
    assertResult(dict2.size)(dict.size)
  }
  
}