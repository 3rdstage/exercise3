
// https://www.typescripttutorial.net/

let msg: string = "Hello, World";
console.log(msg);

interface Product{
  id : number,
  name : string,
  price : number
}

function getProduct(id: number) : Product{
  return {
    id : id,
    name : `Awesome Gadget ${id}`,
    price : 99.5
  }
}

const showProduct = (name: string, price: number) => {
  console.log(`The product '${name}' costs $${price}`);  
}

const prd = getProduct(1);
showProduct(prd.name, prd.price);

// Tuple type
let skill : [string, number];
skill = ['Programming', 10];

// Enum type
enum Month { Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec};
function isItSummer(month: Month) : boolean{
  switch(month){
    case Month.Jun:
    case Month.Jul:
    case Month.Aug:
      return true;
      break;
    default:
      return false;
      break;
  }
};

// Union type
function add(a : string | number, b : string | number) : string | number {
  if(typeof a == 'string' && typeof b == 'string') return a.concat(b);
  else if(typeof a == 'number' && typeof b == 'number') return a + b;
  else throw new Error('Parameters should have same type');
}

// String literal types
let ev: 'click' | 'dbclick' | 'mouseup' | 'mousedown';

// Type aliasing
type alphanumeric = string | number;
let input : alphanumeric

// Function type
type add = (a: number, b: number) => number;
type subtract = (a: number, b: number) => number;
type multiply = (a: number, b: number) => number;
type divide = (a: number, b: number) => number;
function plus(a: number, b: number) : number{
  return a + b;
}
let apply : (a: number, b: number, f: add | subtract | multiply | divide) => number;

// Types for rest parameter
function sum(... nums: number[]): number{
  let sum : number = 0;
  for (const a of nums){ sum =+ a; }
  return sum; 
}

// Type annotations for class
class Person {

  private readonly birthDate: Date;
  private ssn: string;
  private firstName: string;
  private lastName: string;

  constructor(ssn: string, firstName: string, lastName: string, birthDate: Date){
    this.ssn = ssn;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
  }

  public getFullName(): string{
    return `${this.firstName} ${this.lastName}`;
  }

  public getBirthDate(): Date{
    return this.birthDate;
  }

}

// Typescript Generics
function getRandomElement<T>(items: T[]): T {
  let index = Math.floor(Math.random() * items.length);
  return items[index]
}

console.log(getRandomElement<number>([1, 3, 5, 7, 9]));
