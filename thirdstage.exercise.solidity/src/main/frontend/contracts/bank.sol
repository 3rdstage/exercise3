pragma solidity ^0.4.0;

contract Bank {
   
   
   address owner;
   
   mapping (address => uint) balances;
   
   //constructor
   function Bank(){
      owner = msg.sender;
   }
   
   function deposit(address customer) payable returns (bool res){
      
      if(msg.sender != owner) return false;
      balances[customer] += msg.value;
      return true;
   }
   
   function withdraw(address customer, uint amt) returns (bool res){
      if(balances[customer] < amt || amt == 0) return false;
      balances[customer] -= amt;
      msg.sender.send(amt);
      return true;
   }
   
   function remove(){
      if(msg.sender == owner){
         selfdestruct(owner);
      }
   }
}


contract FundManager {
   
   address owner;
   address bank;
   
   function FundManager(){
      owner = msg.sender;
      bank = new Bank();
   }
   
   function deposit() payable returns (bool res){
      if(msg.value == 0) return false;
      
      if(bank == 0x0){
         msg.sender.send(msg.value);
         return false;
      }
      
      Bank bk = Bank(bank);
      bool result = bk.deposit.value(10)(msg.sender);
      
      if(!result){
         msg.sender.send(msg.value);
      }
      return result;
   }
   
   
   function withdraw(uint amt) returns (bool res){
      if(bank == 0x0) return false;
      
      bool result = Bank(bank).withdraw(msg.sender, amt);
      if(result) msg.sender.send(amt);
      
      return result;
   }
   
}
