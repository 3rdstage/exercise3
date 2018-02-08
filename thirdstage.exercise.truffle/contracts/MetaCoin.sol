pragma solidity ^0.4.18;

// https://github.com/truffle-box/metacoin-box/blob/master/contracts/MetaCoin.sol

contract MetaCoin {

  mapping (address => uint) balances;
  
  event Transfer(address indexed _from, address indexed _to, uint256 _val);
  
  
  function MetaCoin() public {
    balances[tx.origin] = 10000;
  }
  
  
  function getBalance(address addr) public view returns(uint){
    return balances[addr];
  }
  
  function sendCoin(address receiver, uint amt) public returns(bool sufficent){
    if (balances[msg.sender] < amt) return false;
    balances[msg.sender] -= amt;
    balances[receiver] += amt;
    Transfer(msg.sender, receiver, amt);
    return true;
  }
}