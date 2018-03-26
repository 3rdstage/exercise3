pragma solidity ^0.4.2;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/lib/LangLib.sol";

// References
//   https://github.com/trufflesuite/truffle/blob/beta/lib/testing/Assert.sol

contract TestStringUtils{

  function testConcat1() public {

    string memory str1 = "Hello, ";
    string memory str2 = "Solidity";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "Hello, Solidity", "StringUtils.concat(stmiring, string) has defect");
  }


  function testConcatWithEmptyLeadingString() public {

    string memory str1 = "";
    string memory str2 = "Solidity";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "Solidity", "StringUtils.concat(string, string) has defect when leading string is empty.");
  }

  function testConcatWithBlankLeadingString() public {

    string memory str1 = "     ";
    string memory str2 = "Solidity";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "     Solidity", "StringUtils.concat(string, string) has defect when leading string is blank.");
  }

  function testConcatWithEmptyLaggingString() public {

    string memory str1 = "Solidity";
    string memory str2 = "";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "Solidity", "StringUtils.concat(string, string) has defect when lagging string is empty.");
  }

  function testConcatWithBlankLaggingString() public {

    string memory str1 = "Solidity";
    string memory str2 = "     ";

    string memory str3 = StringUtils.concat(str1, str2);

    Assert.equal(str3, "Solidity     ", "StringUtils.concat(string, string) has defect when lagging string is blank.");
  }

  function testIsEmptyWithEmptyString() public {
    bool flag = StringUtils.isEmpty("");

    Assert.isTrue(flag, "StringUtils.isEmpty works incorrectly with empty string");
  }

  function testIsEmptyWithBlankString() public {
    bool flag = StringUtils.isEmpty("     ");

    Assert.isFalse(flag, "StringUtils.isEmpty works incorrectly with blank string");
  }

  function testIsEmptyWithNonBlankString() public {
    bool flag = StringUtils.isEmpty(" Hello ");

    Assert.isFalse(flag, "StringUtils.isEmpty works incorrectly with non-blank string");
  }

}