package thirdstage.exercise.scala.basic

import org.scalatest.Finders
import org.scalatest.FunSuite

class ForSuite extends FunSuite {
  
  val digits = 0.to(9)
  
  val fruits = List("Apple", "Banana", "Orange")
  
  val capitals = Map("Korea" -> "Seoul", "USA" -> "Washington", "France" -> "Paris", "UK" -> "London", "China" -> "Beijing")
  
  test("For with if filtering"){

    for(i <- 1 to 100 if i % 3 == 0){
      println(i + " is multiple of 3")
    }

  }

  test("Listing files using for clasue"){

    val files = (new java.io.File(java.lang.System.getenv("windir"))).listFiles()

    println("Files under %windir% directory")
    for(file <- files) println(file)

  }
  
  
  test("Using zipWithIndex with for loop"){
    
    //val fruits = Array("apple", "banana", "orange")
    
    val nos = for((e, no) <- this.fruits.zipWithIndex) yield no
    
    this.assertResult(true)(nos.contains(0))
    this.assertResult(true)(nos.contains(1))
    this.assertResult(true)(nos.contains(2))
    this.assertResult(fruits.length)(nos.length)
  }
  

  test("For loop with a guard"){
    
    val a = for(e <- this.fruits if !e.startsWith("O")) yield e
    
    this.assertResult(true)(a.contains("Apple"))
    this.assertResult(true)(a.contains("Banana"))
    this.assertResult(false)(a.contains("Orange"))
  }
  
  test("For loop with map"){
    
     val countries = (for((k, v) <- this.capitals) yield k).toArray
     
     this.assertResult(true)(countries.contains("Korea"))
     this.assertResult(true)(countries.contains("USA"))
     this.assertResult(false)(countries.contains("Japan"))
  }
  
  test("For loop with multiple guards"){
    
    val a = for{
      i <- this.digits
      if i > 3
      if i < 7
      if i % 2 == 0
    } yield i
    
    this.assertResult(2)(a.length)
    this.assertResult(true)(a.contains(4))
    this.assertResult(true)(a.contains(6))
  }

}