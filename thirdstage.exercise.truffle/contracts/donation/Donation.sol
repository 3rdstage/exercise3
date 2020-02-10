pragma solidity ^0.5.0;

/// @dev https://remix-ide.readthedocs.io/en/latest/tutorial_debug.html
contract Donation {
    
    event FundMoved(address _to, uint _amt);
    
    address owner;
    modifier onlyOwner { if(msg.sender == owner) _; }
    
    
    address[] _givers;
    uint[] _values;
    
    constructor() public{
        owner = msg.sender;
    }
    
    function donate() public payable{
        _addGiver(msg.value);
    }
    
    function _addGiver(uint _amt) internal{
        _givers.push(msg.sender);
        _values.push(_amt);
    }
    
    
    
    
    
}