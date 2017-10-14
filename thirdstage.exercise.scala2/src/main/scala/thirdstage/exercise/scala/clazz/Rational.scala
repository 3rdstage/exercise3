package thirdstage.exercise.scala.clazz

/**
 * @author Sangmoon Oh
 * @since 2016-07-08
 */
class Rational(n: Int, d:Int) {

  require(d != 0)

  val numer: Int = n
  val denom: Int = d

  def this(n: Int) = this(n, 1)
  
  override def toString = n + "/" + d

  def add(that: Rational): Rational =
    new Rational(
        numer * that.denom + denom * that.numer,
        denom * that.denom
    )
  
  def + (that: Rational): Rational = this.add(that)
  
  def * (that: Rational): Rational =
    new Rational(this.numer * that.numer, this.denom * that.denom)
  
}