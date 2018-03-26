package oop

import (
	"testing"
	"time"
)

func TestDeveloperGetName(t *testing.T) {
	steve := Developer{
		Employee{
			"Steve", "John", time.Date(1990, time.February, 17, 0, 0, 0, 0, time.UTC),
			"Software Engineer", "San Francisco",
		},
		[]string{"Go", "Docker", "Java"},
	}

	actual := steve.GetFullName()
	expected := "Steve John"

	if actual != expected {
		t.Errorf("Expected: %d, Actual: %d", expected, actual)
	}

	alex := Manager{
		Employee{
			"Alex", "Williams", time.Date(1979, time.February, 17, 0, 0, 0, 0, time.UTC),
			"Program Manger", "Santa Clara",
		},
		[]string{"CRM", "e-Commerce"},
		[]string{"San Francisco", "Santa Clara"},
	}

	actual = alex.GetFullName()
	expected = "Alex Williams"

	if actual != expected {
		t.Errorf("Expected: %d, Actual: %d", expected, actual)
	}

}
