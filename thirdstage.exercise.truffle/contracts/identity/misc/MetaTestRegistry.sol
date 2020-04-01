//pragma solidity 0.4.15;
pragma solidity ^0.5.0;


// This contract is only used for testing purposes.
contract MetaTestRegistry {

    mapping(address => uint) public registry;

    // for Solidity 0.4, default visibility is 'public', 0.5+ visibility should be explicit
    function register(address sender, uint x) public {  
        registry[sender] = x;
    }
}
