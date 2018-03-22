pragma solidity ^0.4.2;

// Follows StringUtils.class of apache-commons-lang as possible
// https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html?org/apache/commons/lang3/StringUtils.html
/// @title String utilities
library StringUtils{

  /// @notice Concatenates the specified two strings
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

  /// @notice Checks if a specified string is empty ("") or null
  /// @param str string to check
  /// @return `true` if the `str` is empty or null, `false` otherwise
  function isEmpty(string str) internal pure returns(bool){
    return (bytes(str).length == 0);
  }

}