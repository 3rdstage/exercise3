pragma solidity ^0.5.0;


contract Faucet{

  private address owner;

  constructor(){
    owner = msg.sender;

  }

  function withdraw(uint amt) public {
    require(amt <= 100000000000000000);

    msg.sender(amt);
  }


  function() public payable{

  }

  function destroy() public{
    require(msg.sender == owner);
    selfdestruct(owner);
  }


}
