package oop

import (
	"fmt"
	"time"
)

type Employee struct {
	FirstName string
	LastName  string
	Dob       time.Time
	JobTitle  string
	Location  string
}

func (e Employee) GetFullName() string {
	return fmt.Sprintf("%s %s", e.FirstName, e.LastName)
}

func (e Employee) GetDetail() string {
	return fmt.Sprintf("%s %s, %s, %s", e.FirstName, e.LastName, e.JobTitle, e.Location)
}

type Developer struct {
	Employee
	Skills []string
}

type Manager struct {
	Employee
	Projects  []string
	Locations []string
}
