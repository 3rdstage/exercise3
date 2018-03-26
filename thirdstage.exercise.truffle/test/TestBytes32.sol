pragma solidity ^0.4.2;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";


contract TestBytes32{

  function testLength() public {
    bytes32 str = "Hello, World";
    uint l = str.length;

    Assert.equal(l, 32, "The length of bytes32 can be other than 32.");
  }

  // The length of bytes32 type variable is always 32
  function testCharAtOfStringLiteral1() public {
    bytes32 str = "Hello, World";

    //var ch = str[0];
    Assert.equal(str[0], "H", "Index access for bytes32 is strange.");
    Assert.equal(str[1], "e", "Index access for bytes32 is strange.");
    Assert.equal(str[2], "l", "Index access for bytes32 is strange.");
    Assert.equal(str[5], ",", "Index access for bytes32 is strange.");
    Assert.equal(str[6], " ", "Index access for bytes32 is strange.");
  }

  function testPaddingOfStringLiteral1() public {
    bytes32 str = "Hello, World";

    Assert.equal(str[12], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");
    Assert.equal(str[13], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");
    Assert.equal(str[31], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");
  }

  function testCharAtOfNumberLiteral1() public {
    bytes32 str = 0x41424344;  // "ABCD"

    Assert.equal(str[28], "A", "Index access for bytes32 type number literal is strange.");
    Assert.equal(str[29], "B", "Index access for bytes32 type number literal is strange.");
    Assert.equal(str[30], "C", "Index access for bytes32 type number literal is strange.");
    Assert.equal(str[31], "D", "Index access for bytes32 type number literal is strange.");
  }

  function testPaddingOfNumberLiteral1() public {
    bytes32 str = 0x41424344;  // "ABCD"

    Assert.equal(str[0], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");
    Assert.equal(str[1], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");
    Assert.equal(str[2], 0x00, "Padding character for bytes32 is not 0x00(Ascii NUL).");

  }

}