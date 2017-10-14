package thirdstage.exercise.scala.generic

/**
 * @param minLowerLimit
 * @param minDefault
 * @param maxDefault
 * @param maxUpperLimit
 * @param evidence$2
 */
//@TODO Alternatively try to define as a case class
class RangeSpec[T <% Ordered[T]] private {
  
  private var minLowerLimit: T = _
  
  private var minDefault: T = _
  
  private var maxUpperLimit: T = _
  
  private var maxDefault: T = _
  
  private def this(minLowerLimit: T, minDefault: T, maxDefault: T, maxUpperLimit: T){
    this()
    
    if(minLowerLimit > minDefault){
      throw new IllegalArgumentException()
    }
    
    this.minLowerLimit = minLowerLimit
    this.minDefault = minDefault
    this.maxUpperLimit = maxUpperLimit
    this.maxDefault = maxDefault
    
  }
  
  def getMinLowerLimit = minLowerLimit
  
  def getMinDeault = { minDefault }
  
  def getMaxDefault = { maxDefault }
  
  def getMaxUpperLimit = { maxUpperLimit }
  
  
}

object RangeSpec{
  
  def apply[T <% Ordered[T]](minLowerLimit: T, minDefault: T, maxDefault: T, maxUpperLimit: T) = {
    val spec = new RangeSpec(minLowerLimit, minDefault, maxDefault, maxUpperLimit)
    spec
  }
}

