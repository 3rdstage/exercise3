package password

type CharacterPolicy int

const (
	AlphaNum             CharacterPolicy = 0
	AlphaNumSpecial      CharacterPolicy = 1
	UpperLowerNum        CharacterPolicy = 2
	UpperLowerNumSpecial CharacterPolicy = 3
)

var SpecialCharacters = []rune{'!', '@'}

func (p CharacterPolicy) isValidSpecialCharacter(char rune) bool {

	return true
}

type Constraints struct {
	MinLength         int
	MaxLenght         int
	CharPolicy        CharacterPolicy
	MaxContinousChars int
	MaxSameChars      int
}
