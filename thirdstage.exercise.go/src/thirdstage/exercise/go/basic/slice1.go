package main

import "fmt"

func main() {
	var a []int
	a = []int{1, 2, 3}

	a[1] = 10

	fmt.Println(a)
}
