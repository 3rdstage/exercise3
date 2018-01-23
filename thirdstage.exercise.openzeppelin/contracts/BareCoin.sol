pragma solidity ^0.4.2;

// http://solidity.readthedocs.io/en/develop/introduction-to-smart-contracts.html
/// @title Simplest coin example
contract BareCoin {

  address public minter;

  mapping(address => uint) public balances;

  event Sent(address from, address to, uint mount);

  function BareCoin() public {
    minter = msg.sender;
  }

  function mint(address receiver, uint amount) public {
    if(msg.sender != minter) return;
    balances[receiver] += amount;
  }


  function send(address receiver, uint amount) public {
    if(balances[msg.sender] < amount) return;
    balances[msg.sender] -= amount;
    balances[receiver] += amount;
    Sent(msg.sender, receiver, amount);
  }
}