//pragma solidity 0.4.15;
pragma solidity ^0.5.0;

import "./libs/Owned.sol";


contract Proxy is Owned {
    event Forwarded (address indexed destination, uint value, bytes data);
    event Received (address indexed sender, uint value);

    function () payable external { 
        emit Received(msg.sender, msg.value); 
        
    }

    function forward(address destination, uint value, bytes memory data) public onlyOwner {
        require(executeCall(destination, value, data));
        emit Forwarded(destination, value, data);
    }

    // copied from GnosisSafe
    // https://github.com/gnosis/gnosis-safe-contracts/blob/master/contracts/GnosisSafe.sol
    function executeCall(address to, uint256 value, bytes memory data) internal returns (bool success) {
        assembly {
            success := call(gas, to, value, add(data, 0x20), mload(data), 0, 0)
        }
    }
}
