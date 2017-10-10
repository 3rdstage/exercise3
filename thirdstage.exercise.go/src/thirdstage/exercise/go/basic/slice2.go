package main

func main2(){
	var s []int

  if s == nil {
  	println("Nil Slice")
  } else {
  	println("Length: ", len(s), ", Capacity: ", cap(s))
  }


}