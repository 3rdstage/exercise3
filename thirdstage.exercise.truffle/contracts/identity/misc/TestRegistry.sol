// This contract is only used for testing purposes.
//pragma solidity 0.4.15;
pragma solidity ^0.5.0;


contract TestRegistry {

    mapping(address => uint) public registry;
    mapping(address => string) public strRegistry;

    function register(uint x) public {
        registry[msg.sender] = x;
    }

    function reallyLongFunctionName(
        uint with,
        address many,
        string memory strange,
        uint params
    ) public {
        strRegistry[many] = strange;
        registry[many] = with;
        registry[many] = params;
    }

    function testThrow() public {
        revert();
    }
}
