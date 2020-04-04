pragma solidity ^0.5;

library ECDSA {

  // Reference : https://dzone.com/articles/signing-and-verifying-ethereum-signatures

  function recover(bytes32 hash, bytes memory signature) internal pure returns(address){

    bytes32 r;
    bytes32 s;
    uint8 v;

    //check the signature length
    if(signature.length != 65){ return (address(0)); }

    assembly {
      r := mload(add(signature, 0x20))
      s := mload(add(signature, 0x40))
      v := byte(0, mload(add(signature, 0x60)))
    }

    if(v < 27){ v += 27; }

    if(v != 27 && v != 28){ return address(0); }
    else {
        return ecrecover(hash, v, r, s);
    }
   }


   function toEthSignedMessageHash(bytes32 hash) internal pure returns(bytes32){
     return keccak256(abi.encodePacked("\x19Ethereum Signed Message:\x32", hash));
   }

}
