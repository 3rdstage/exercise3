use std::io;
use std::cmp::Ordering;
use rand::Rng;

fn main() {
    println!("Guess the number!");
    
    let secret_num = rand::thread_rng().gen_range(1..101);
    
    println!("The secret number is : {}", secret_num);
    
    loop{
      println!("Please input your guess.");
    
      let mut guess = String::new();
    
      io::stdin()
        .read_line(&mut guess)
        .expect("Fail to read line");

      let guess: u32 = guess.trim().parse().expect("Please type a number!");      
      
      match guess.cmp(&secret_num) {
        Ordering::Less => println!("Too small!"), 
        Ordering::Greater => println!("Too big!"),
        Ordering::Equal => {
          println!("You win!");
          break;
        },
      }
    }
}
