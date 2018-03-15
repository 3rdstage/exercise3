package oop

type Employee1 struct {
	firstName   string
	lastName    string
	totalLeaves int
	takenLeaves int
}

func NewEmployee1(firstName string, lastName string, totalLeaves int, takenLeaves int) Employee1 {
	return Employee1{firstName, lastName, totalLeaves, takenLeaves}
}

func (emp Employee1) Create(firstName string, lastName string, totalLeaves int, takenLeaves int) Employee1 {
	return Employee1{firstName, lastName, totalLeaves, takenLeaves}
}

func (emp Employee1) RemainingLeaves() int {
	return (emp.totalLeaves - emp.takenLeaves)
}
