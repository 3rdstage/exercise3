pragma solidity ^0.5.0;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";


contract TestBasic{

    mapping(uint256 => address) winners;
    mapping(uint256 => string) levelCodes;

    function beforeAll() public {
        winners[0] = msg.sender;
        
        levelCodes[0] = "Low";
        levelCodes[1] = "Medium";
        levelCodes[2] = "High";
    }


    function testAddressTypeZeroValue() public {
        address add1;
        
        Assert.equal(add1, address(0), "Unassigned(initialized by VM not by user) local address variable has other value than zero address.");

    }
    
    function testMappingEntryZeroValue() public {
        
        Assert.notEqual(winners[0], address(0), "BeforeAll has not run, which is never expected.");
        Assert.equal(winners[1], address(0), "Address type value for a non existing key in mapping is not Zero address.");
        Assert.equal(winners[100], address(0), "Address type value for a non existing key in mapping is not Zero address.");
        
        
        Assert.equal(levelCodes[2], "High", "Oops.");
        Assert.equal(levelCodes[3], "", "String type value for a non existing key in mapping is not empty string.");
        Assert.equal(levelCodes[100], "", "String type value for a non existing key in mapping is not empty string.");
    }
    
    
    
}