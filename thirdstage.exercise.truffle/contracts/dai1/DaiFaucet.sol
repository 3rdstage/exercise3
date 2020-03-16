pragma solidity ^0.5.9;

// This smart contract is from 'https://github.com/makerdao/developerguides/tree/master/dai/dai-in-smart-contracts'
// This smart constract should deplyed to 'Kovan' testnet

// References
//   - https://github.com/makerdao/dss/blob/1.0.3/src/dai.sol

interface DaiToken{
  function transfer(address dest, uint wad) external returns (bool);
  function balanceOf(address owner) external view returns (uint);
}

contract owned{
  DaiToken _token;
  address _owner;

  constructor() public {
    _owner = msg.sender;

    _token = DaiToken(0x4F96Fe3b7A6Cf9725f59d353F723c1bDb64CA6Aa);
  }

  modifier onlyOwner{
    require(msg.sender == _owner, "Only the contract owner can call this function.");
    _;
  }

}

contract mortal is owned{
  function destroy() public onlyOwner{
    _token.transfer(_owner, _token.balanceOf(address(this)));
    selfdestruct(msg.sender);
  }
}

contract DaiFaucet is mortal {

  event Withrawal(address indexed to, uint amt);
  event Deposit(address indexed from, uint amt);

  function withraw(uint amt) public{
    require(amt <= 0.1 ether);
    require(_token.balanceOf(address(this)) >= amt, "Insufficient balance in this faucet for the requested amount");
    _token.transfer(msg.sender, amt);
    emit Withrawal(msg.sender, amt);
  }

  function () external payable{
    emit Deposit(msg.sender, msg.value);
  }

}
