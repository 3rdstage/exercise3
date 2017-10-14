package thirdstage.exercise.scala.clazz

class ChecksumAccumulator {
  
  private var sum = 0
  
  def add(b: Byte) { sum += b }
  
  def checksum() = ~(sum & 0xFF) + 1
  
}