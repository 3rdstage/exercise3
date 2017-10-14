package thirdstage.exercise.scala.password

import javax.annotation.concurrent.Immutable
import org.apache.commons.lang3.Validate
import javax.validation.constraints.Min
import javax.validation.constraints.Max
import thirdstage.exercise.scala.password.CharacterPolicy
import javax.annotation.Nonnull

@Immutable
class PasswordFeatures private {
  
  private var minLength = PasswordFeatures.LengthMinLimit
  
  private var maxLength = PasswordFeatures.LengthMaxDefault

  private var charPolicy = PasswordFeatures.CharacterPolicyDefault
  
  private var specialChars = Array[Char]()

  private var maxContinousChars = PasswordFeatures.ContinuousCharsMaxDefault
  
  private var maxSameChars = PasswordFeatures.SameCharsMaxDefault
  
  def getMinLength = minLength 
  
  def getMaxLength = maxLength
  
  def getCharacterPolicy = charPolicy
  
  def getMaxContinousChars = maxContinousChars
  
  def getMaxSameChars = maxSameChars
  
}

object PasswordFeatures {
  
  val LengthMinLimit = 4
  
  val LenghMinDefault = LengthMinLimit
  
  val LengthMaxLimit = 16
  
  val LengthMaxDefault = 12
  
  var CharacterPolicyDefault = CharacterPolicy.AlphaNum
  
  val ContinuousCharsMaxLowerLimit = 2
  
  val ContinuousCharsMaxDefault = 3
  
  val SameCharsMaxLowerLimit = ContinuousCharsMaxLowerLimit
  
  val SameCharsMaxDefault = ContinuousCharsMaxDefault
  
  
  def apply(minLen: Integer, maxLen: Integer, 
      @Nonnull charPolicy: CharacterPolicy.Value, specialChars: Array[Char],
      continChasrMax: Integer, sameCharsMax: Integer) = {
    Validate.isTrue(minLen >= LengthMinLimit, "The min length should not less than ${LengthMinLimit}.")
    Validate.isTrue(maxLen <= LengthMaxLimit, "The max length should not more than ${LengthMaxLimit}.")
    Validate.isTrue(minLen <= maxLen, "The min length should not more than max length.")
    Validate.isTrue(charPolicy != null, "The character policy should be specified.")
    
    //@TODO validate specialChars
    
    Validate.isTrue(continChasrMax >= ContinuousCharsMaxLowerLimit, 
        "The max number of continous adjacent characters should not less than ${ContinuousCharsMaxLowerLimit}.")
    //@TODO validatee sameCharMax
    
    var features = new PasswordFeatures
    features.minLength = minLen
    features.maxLength = maxLen
    features.charPolicy = charPolicy
    features.specialChars = specialChars
    features.maxContinousChars = continChasrMax
    features.maxSameChars = sameCharsMax
    features
  }
  
  def apply(minLen: Integer, maxLen: Integer): PasswordFeatures = {
    apply(minLen, maxLen, CharacterPolicyDefault, Array[Char](), 
        ContinuousCharsMaxDefault, SameCharsMaxDefault)
 
  }
  
  def apply(minLen: Integer): PasswordFeatures = {
    apply(minLen, LengthMaxDefault)
  }
  
}