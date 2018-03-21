pragma solidity ^0.4.2;

library StringUtils{

  function concat(string str1, string str2) internal pure returns(string){
    bytes memory b1 = bytes(str1);
    bytes memory b2 = bytes(str2);

    string memory str = new string(b1.length + b2.length);
    bytes memory b = bytes(str);

    uint p = 0;
    for(uint i = 0; i < b1.length; i++){ b[p++] = b1[i]; }
    for(i = 0; i < b2.length; i++){ b[p++] = b2[i]; }

    return string(b);
  }
}