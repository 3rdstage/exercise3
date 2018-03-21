pragma solidity ^0.4.2;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/lib/LangLib.sol";


contract TestStringUtils{

  function testConcat1() public {

    string memory str1 = "Hello ";
    string memory str2 = "Solidity";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "Hello Solidity", "StringUtils.concat has defect");
  }


}