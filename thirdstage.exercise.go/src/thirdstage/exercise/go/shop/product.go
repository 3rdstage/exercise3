package shop

import ()

type Product struct {
	id   string
	name string
	desc string
}

type SalesOffering struct {
	product    Product
	providerId string
	price      uint
	remainders uint
}
