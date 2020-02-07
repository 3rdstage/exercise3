pragma solidity ^0.5.0;

import "./IERC721.sol";

/// 
/// @dev https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/token/ERC721/ERC721.sol
contract ERC721 is IERC721{

        
    // token -> owner map
    mapping(uint256 => address) private _owners;

    // token -> approved delegator map
    mapping(uint256 => address) private _delegators;

    // owner -> # of tokens map
    mapping(address => uint256) private _counts;  //derived
    
    
    // owner -> apporver -> yes/no
    mapping(address => mapping(address => bool)) private _operators;
    
    /// @dev override
    function balanceOf(address _owner) public view returns(uint256){
        require(_owner != address(0), "");
        
        return _counts[_owner];
    }
    
    function ownerOf(uint256 _tokenId) public view returns(address){
        address owner = _owners[_tokenId];
        
        require(owner != address(0), "The owner is ZERO account which means the token is useless");
        return owner;
    }
    
    
    function approve(address _delegator, uint256 _tokenId) public {
        address owner = ownerOf(_tokenId);
        
        require(msg.sender == owner || isApprovalForAll(owner, msg.sender), "Only token owner or operator can change the apporved delegator of the token.");
        require(_delegator != owner, "ERC 721 token transfer can't be approved to the owner");

        _delegators[_tokenId] = _delegator;
        emit Approval(owner, _delegator, _tokenId);
    }
    
    
    function getApproved(uint256 _tokenId) public view returns(address){
        
        
    }
    
    function setApprovalForAll(address _to, bool _approved) public {
        require(_to != msg.sender, "The owner can't be operator");
        
        _operators[msg.sender][_to] = _approved;
        
        emit ApprovalForAll(msg.sender, _to, _approved);
    }
    
    
    function isApprovalForAll(address _owner, address _operator) public view returns (bool){
        return _operators[_owner][_operator];
    }
    
    
    function _existsToken(uint256 tokenId) internal view returns (bool){
        return _owners[tokenId] != address(0);
    }
    
    function _isApprovedOrOwner(address _spender, uint256 _tokenId) internal view returns (bool){
        require(_existsToken(_tokenId), "Specified token doesn't exist.");
        address owner = _owners[_tokenId];
        
        return ( _spender == owner || _spender == getApproved(_tokenId) || isApprovedForAll(owner, _spender));
    }
    
    
    function _transferFrom(address _from, address _to, uint256 _tokenId) internal {
        require(_to != address(0), "Unable to transfer to ZERO address.");
        require(_owners[_tokenId] == _from, "The specified from address is not the owner of the specified token.");
        require(_isApprovedOrOwner(msg.sender, _tokenId), "The message sender is neither the owner or approved delegator for the specified token.");
        
        
        
    }
    
    function transferFrom(address _from, address _to,  uint256 _tokenId) public {
        
    }
    
    
    
    
    
}