//pragma solidity ^0.4.18;
pragma solidity ^0.5.0;

// https://github.com/truffle-box/metacoin-box/blob/master/contracts/MetaCoin.sol


/// @title Meta Coin
/// @notice Simplest coint to show the basic operation, not for production
contract MetaCoin {

  mapping (address => uint) balances;

  event Transfer(address indexed _from, address indexed _to, uint256 _val);


  constructor() public {
    balances[tx.origin] = 10000;
  }

  /// @notice Get the balance of the specified account
  /// @param addr the address of the account
  /// @return the current balance of the specified account
  function getBalance(address addr) public view returns(uint){
    return balances[addr];
  }

  /// @notice Transfer the balance from the sender's account to the specified account
  /// @param receiver the receiver's account
  /// @param amt the amount to transfer
  /// @return `true` if succeeded, or `false` otherwise
  function sendCoin(address receiver, uint amt) public returns(bool sufficent){
    if (balances[msg.sender] < amt) return false;
    balances[msg.sender] -= amt;
    balances[receiver] += amt;
    emit Transfer(msg.sender, receiver, amt);
    return true;
  }
}
