pragma solidity ^0.5.0;

import "./IERC20.sol";

/**
 *      Sender -------- recipient
 *        |
 *   +---+---+
 *  |        |
 * owner   allowee
 *
 */
/// @title Abstract ERC20 Token
contract AbstractERC20 is IERC20{
    
  string private _name;
  
  string private _symbol;
  
  uint8 private _decimals;
  
  bool private _initialized;

  uint256 private _totalSupply;

  mapping(address => uint256) private _balances; // balance by account

  mapping(address => mapping(address => uint256)) private _allowances; // allowance for each allowee by onwer account  - owner.allowee

  
  /// @dev Intialize `name` and `symbol` of this Token
  /// @param name Token name - shouldn't be empty or blank
  function init(string memory name, string memory symbol) internal returns (bool){
      require(bytes(name).length > 0, "Empty name is disallowed.");
      require(bytes(symbol).length > 0, "Empty symbol is disallowed.");
      assert(!_initialized);  // name, symbol cannot be chaned after initialization
      
      _name = name;
      _symbol = symbol;
      _initialized = true;
      return true;
  }
  
  function init(string memory name, string memory symbol, uint8 decimals) internal returns (bool){
      assert(!_initialized); // name, symbol, decimals cannot be changed after initialization
      _decimals = decimals;
      
      return init(name, symbol);
  }
  
  
  /// @dev `name()` function defined in EIP 20 (https://eips.ethereum.org/EIPS/eip-20)
  function name() public view returns(string memory){
      return _name;
  }
    
  /// @dev `symbol()` function defined in EIP 20 (https://eips.ethereum.org/EIPS/eip-20)
  function symbol() public view returns(string memory){
      return _symbol;
  }

  /// @dev `decimals()` function defined in EIP 20 (https://eips.ethereum.org/EIPS/eip-20)
  function decimals() public view returns(uint8){
      return _decimals;
  }

  function totalSupply() public view returns (uint256){
    return _totalSupply;
  }

  function balanceOf(address acct) public view returns (uint256){
    return _balances[acct];
  }

  function transfer(address recipient, uint256 amt) public returns (bool){
    _transfer(msg.sender, recipient, amt);
    return true;

  }

  // Is it okay the allowee is actaully the owner?
  function allowance(address owner, address allowee) public view returns(uint256){
    return _allowances[owner][allowee];

  }
  
  // msg.sender = owner
  function approve(address allowee, uint256 amt) public returns(bool){
    _approve(msg.sender, allowee, amt);
    return true;
  }
  
  
  // msg.sender = allowee
  function transferFrom(address owner, address recipient, uint256 amt) public returns(bool){
    _transfer(owner, recipient, amt);
    
    require(_allowances[owner][msg.sender] >= amt, "The message sender doesn't enough allowance from the owner");
    _approve(owner, msg.sender, _allowances[owner][msg.sender] - amt);
  }
  
  
  // msg.sender = owner
  function increaseAllowance(address allowee, uint256 delta) public returns(bool){
    require(_allowances[msg.sender][allowee] + delta >= _allowances[msg.sender][allowee], "The allowance for the allowee from the current address is overflowed.");
    _approve(msg.sender, allowee, _allowances[msg.sender][allowee] + delta);
    return true;
  }
  
  // msg.sender = owner
  function decreseAllowance(address allowee, uint256 delta) public returns(bool){
    require(_allowances[msg.sender][allowee] >= delta, "The allowance for the allowee from the current address is not enough");
    _approve(msg.sender, allowee, _allowances[msg.sender][allowee] - delta);
    return true;
  }
  
  

  function _transfer(address sender, address recipient, uint256 amt) internal{
    require(sender != address(0), "Sender shouldn't be ZERO address");
    require(recipient != address(0), "Recipient shouldn't be ZERO address");

    require(_balances[sender] >= amt, "Sender has NOT enough balance.");
    require(_balances[recipient] + amt >= _balances[recipient], "Recipient balance would be overflowed if transfered.");

    _balances[sender] = _balances[sender] - amt;
    _balances[recipient] = _balances[recipient] + amt;

    emit Transfer(sender, recipient, amt);
  }

  function _approve(address owner, address allowee, uint256 amt) internal{

    require(owner != address(0), "Owner couldn't be ZERO address. ");
    require(allowee != address(0), "Allowee(spender) couldn't be ZERO address.");
    
    _allowances[owner][allowee] = amt;
    
    emit Approval(owner, allowee, amt);


  }


}