pragma solidity ^0.5.0;

// The most basic shape of ERC 20 Token
// References
//   - https://eips.ethereum.org/EIPS/eip-20
//   - https://docs.openzeppelin.com/contracts/2.x/erc20
//   - https://docs.openzeppelin.com/contracts/2.x/api/token/erc20
//   - https://github.com/OpenZeppelin/openzeppelin-contracts/tree/master/contracts/token/ERC20


/**
 *      Sender -------- recipient
 *        |
 *   +---+---+
 *  |        |
 * owner   allowee
 *
 */
interface IERC20{

  function totalSupply() external view returns (uint256);


  function balanceOf(address acct) external view returns (uint256);

  // requires caller == owner
  function transfer(address recipient, uint256 amt) external returns (bool);

  function allowance(address owner, address allowee) external view returns (uint256);

  function approve(address allowee, uint256 amt) external returns (bool);

  // This can be used for example to allow a contract to transfer tokens on your behalf and/or to charge fees in sub-currencies.
  // requires allowance(sender, caller) >= amt
  function transferFrom(address sender, address recipient, uint256 amt) external returns (bool);

  event Transfer(address indexed from, address indexed to, uint256 value);

  event Approval(address indexed owner, address indexed allowee, uint256 value);

}
