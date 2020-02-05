pragma solidity ^0.5.0;

import "./IERC721.sol";

/// 
/// https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/token/ERC721/ERC721.sol
contract ERC721 is IERC721{

        
    // token -> owner map
    mapping(uint256 => address) private _tokenOwners;

    // token -> approver map
    mapping(uint256 => address) private _tokenApprovers;

    // owner -> # of tokens map
    mapping(address => uint256) private _tokenCounts;
    
    
    // owner -> apporver -> yes/no
    mapping(address => mapping(address => bool)) private _operators;
    
    /// @override
    function balanceOf(address _owner) public view returns(uint256){
        require(_owner != address(0), "");
        
        return _tokenCounts[_owner];
    }
    
    function ownerOf(uint256 _tokenId) public view returns(address){
        address owner = _tokenOwners[_tokenId];
        
        require(owner != address(0), "");
        return owner;
    }
    
    
    function approve(address _approved, uint256 _tokenId) public {

        
    }
    
    
    
    
    
    
    
}