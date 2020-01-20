pragma solidity ^0.5.0;

import "./Deposit.sol";


contract TransferableDeposit{
    
    
    Deposit private _deposit;
    
    constructor(Deposit deposit) public{
        _deposit = deposit;
    }
    
    
    function deposit(address owner, uint256 amt) public returns(uint256){
        
    }
    
    
    function transfer(address recipient, uint256 amt) public returns(bool){
        
        
        
    } 
    
    
}