pragma solidity ^0.4.2;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/lib/LangLib.sol";

// References
//   https://github.com/trufflesuite/truffle/blob/beta/lib/testing/Assert.sol

contract TestBytes32Utils{

  function testConcat1() public {
    bytes32 b1 = "Hello, ";
    bytes32 b2 = "Solidity";

    bytes memory r = Bytes32Utils.concat(b1, b2);

    Assert.equal(r.length, 14, "Bytes32Utils.concat works incorrectly.");
    Assert.equal(string(r), "Hello, Solidity", "Bytes32Utils.concat works incorrectly.");
  }


}