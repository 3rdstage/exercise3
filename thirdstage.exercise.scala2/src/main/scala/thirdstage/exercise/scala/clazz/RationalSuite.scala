package thirdstage.exercise.scala.clazz

import org.scalatest.FunSuite

/**
 * @author Sangmoon Oh
 * @since 2016-07-08
 */
class RationalSuite extends FunSuite{
  
  test("one half plus two thirds makes seven sixths"){
    val half = new Rational(1, 2)
    val twoThirds = new Rational(2, 3)
    
    val plus = half.add(twoThirds);
    
    this.assertResult(7)(plus.numer)
    this.assertResult(6)(plus.denom)
  }

}