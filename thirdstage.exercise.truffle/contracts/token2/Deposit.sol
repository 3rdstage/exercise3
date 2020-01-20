pragma solidity ^0.5.0;


/// @dev https://en.wikipedia.org/wiki/Deposit_account
contract Deposit{
    
    
    mapping(address => uint256) private _balances;
    
    uint256 private _total;
    
    function deposit(address owner, uint256 amt) public returns(uint256){ // need priviledge
        require(owner != address(0), "The owner address shoud NOT ZERO address." );
        require(_balances[owner] + amt >= _balances[owner], "The balance for the sepcified account has overflowed.");
        require(_total + amt >= _total, "The total amount of this contract has overflowed.");
        
        _balances[owner] = _balances[owner] + amt;
        _total = _total + amt;
        return _balances[owner];
    }
    
    
    function withraw(address owner, uint256 amt) public returns(uint256){
        
    }
    
}