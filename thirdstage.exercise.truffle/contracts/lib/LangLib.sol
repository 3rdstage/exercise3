//pragma solidity ^0.4.2;
pragma solidity ^0.5.0;

// Follows StringUtils.class of apache-commons-lang as possible
// https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html?org/apache/commons/lang3/StringUtils.html

/// @title Provides utilities for `string` type.
library StringUtils{

  /// @notice Concatenates the specified two strings.
  function concat(string memory str1, string memory str2) internal pure returns(string memory){
    bytes memory b1 = bytes(str1);
    bytes memory b2 = bytes(str2);

    string memory str = new string(b1.length + b2.length);
    bytes memory b = bytes(str);

    uint p = 0;
    for(uint i = 0; i < b1.length; i++){ b[p++] = b1[i]; }
    for(uint i = 0; i < b2.length; i++){ b[p++] = b2[i]; }

    return string(b);
  }

  /// @notice Checks if a specified string is empty ("") or null.
  /// @param str string to check
  /// @return `true` if the `str` is empty or null, `false` otherwise
  function isEmpty(string memory str) internal pure returns(bool){
    return (bytes(str).length == 0);
  }

}

/// @title Provides utilities for `bytes32` type
library Bytes32Utils{

  /// @notice Concatenates the specified two `byte32` strings.
  function concat(bytes32 a, bytes32 b) internal pure returns(bytes memory){

    bytes memory c;
    uint i;
    uint p = 0;

    for(i = 0; i < 32; i++){
      if(a[i] != 0x00){ c[p++] = a[i]; }
    }

    for(i = 0; i < 32; i++){
      if(b[i] != 0x00){ c[p++] = b[i]; }
    }

    return c;
  }
}
