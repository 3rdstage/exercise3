package thirdstage.exercise.scala.password

import util.control.Breaks._

/**
 *
 * @version 1.0, 2017-02-22, Sangmoon Oh, Initial shape
 * @author Sangmoon Oh
 * @since 2017-02-22
 *
 */
object CharacterPolicy extends Enumeration {

  type CharacterPolicy = Value

  /**
   * The characters of password should contain at least one alphabet([a-zA-Z])
   * and one digit([0-9]) respectively.
   */
  val AlphaNum = Value

  /**
   * The characters of password should contain at least one alphabet([a-zA-Z])
   * , one digit([0-9]) and one special character respectively.
   * <p>
   * The scope of special character could be defined independently.
   */
  val AlphaNumSpecial = Value

  /**
   * The characters of password should contain at least one upper-case alphabet([A-Z])
   * , one lower-case alphabet([a-z]) and one digit([0-9]) respectively.
   *
   */
  val UperLowerNum = Value

  /**
   * The characters of password should contain at least one upper-case alphabet([A-Z])
   * , one lower-case alphabet([a-z]), one digit([0-9]) and one special character respectively.
   * <p>
   * The scope of special character could be defined independently.
   */
  val UpserLowerNumSpecial = Value

  val SpecialCharacters = (('!' to '/') :+ (':' to '@') :+ ('[' to '`') :+ ('{' to '~')).toArray

  def isValidSpecialCharacter(char: Char) = {
    SpecialCharacters.contains(char)
  }

  def isValidSpecialCharacters(chars: Array[Char]) = {
    var result = true

    breakable {
      for (char <- chars) {
        if (!SpecialCharacters.contains(char)) {
          result = false
          break
        }
      }
    }
    result
  }
  
  def isValidChracter(char: Char) = {
    //@TODO
    
  }
  
  def isValidCharacters(char: Array[Char]) = {
    //@TODO
  }

}