program solidity ^0.5.0;

// The most basic shape of ERC 20 Token
// References
//   - https://eips.ethereum.org/EIPS/eip-20
//   - https://docs.openzeppelin.com/contracts/2.x/erc20
//   - https://docs.openzeppelin.com/contracts/2.x/api/token/erc20
//   - https://github.com/OpenZeppelin/openzeppelin-contracts/tree/master/contracts/token/ERC20


/**
 *     spender ----- owner/sender ---- recipient
 */
interface IERC20{

  function totalSupply() external view returns (uint256);


  function balanceOf(address acct) external view returns (uint256);

  // requires caller/spender == owner
  function transfer(address recipient, uint256 amt) external returns (bool);

  function allowance(address owner, address spender) external view returns (uint256);

  function approve(address spender, uint256 amt) external returns (bool);

  // This can be used for example to allow a contract to transfer tokens on your behalf and/or to charge fees in sub-currencies.
  // requires allowance(sender, caller) >= amt
  function transferFrom(address sender, address recipient, uint256 amt) external returns (bool);

  event Transfer(address index from, address index to, uint256 value);

  event Approval(address index owner, address index spender, uint256 value);

}
