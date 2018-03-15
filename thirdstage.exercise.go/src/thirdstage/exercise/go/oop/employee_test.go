package oop

import (
	"testing"
)

func TestEmplyee1RemainingLeaves(t *testing.T) {
	emp := NewEmployee1("Sangmoon", "Oh", 25, 5)
	expected := 20
	actual := emp.RemainingLeaves()

	if actual != expected {
		t.Errorf("Expected: %d, Actual: %d", expected, actual)
	}
}
