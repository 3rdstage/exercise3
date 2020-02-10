pragma solidity ^0.5.0;

import "./IERC721.sol";

/// @dev refer https://eips.ethereum.org/EIPS/eip-721
/// @dev refer https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/token/ERC721/ERC721.sol
contract ERC721 is IERC721{
    
    address _admin;

    // token -> owner map
    mapping(uint256 => address) private _owners;

    // token -> approved delegator map
    mapping(uint256 => address) private _approvals;

    // owner -> # of tokens map
    mapping(address => uint256) private _counts;  //derived

    // owner -> apporver -> yes/no
    mapping(address => mapping(address => bool)) private _operators;

    
    mapping(bytes4 => bool) private _supportedInterfaces;

    bytes4 private constant _INTERFACE_ID_ERC165 = 0x01ffc9a7;
    
    bytes4 private constant _INTERFACE_ID_ERC721 = 0x80ac58cd;

    constructor() public{
        _admin = msg.sender;
        
        _registerInterface(_INTERFACE_ID_ERC165);
        _registerInterface(_INTERFACE_ID_ERC721);        
    }
    
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
    
    
    function approve(address _approvee, uint256 _tokenId) public {
        address owner = ownerOf(_tokenId);
        
        // Additionally, an authorized operator may set the approved address for an NFT. 
        require(msg.sender == owner || isApprovedForAll(owner, msg.sender), "Only token owner or operator can change the apporved delegator of the token.");
        require(_approvee != owner, "ERC 721 token transfer can't be approved to the owner");

        _approvals[_tokenId] = _approvee;
        emit Approval(owner, _approvee, _tokenId);
    }
    
    
    function getApproved(uint256 _tokenId) public view returns(address){
        require(_existsToken(_tokenId), "Nonexistent token.");
        
        return _approvals[_tokenId];
        
    }
    
    function setApprovalForAll(address _to, bool _approved) public {
        require(_to != msg.sender, "The owner can't be operator");
        
        _operators[msg.sender][_to] = _approved;
        
        emit ApprovalForAll(msg.sender, _to, _approved);
    }
    
    
    function isApprovedForAll(address _owner, address _operator) public view returns (bool){
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
        
        _clearApproval(_tokenId);
        
        uint256 n1 = _counts[_from];
        uint256 n2 = _counts[_to];
        
        require( n1 - 1 < n1, "The token count for '_from' can't be decreased.");
        require( n2 + 1 > n2, "The token count for '_to' overflowed.");
        
        _counts[_from] = n1 - 1;
        _counts[_to] = n2 + 1;
        
        _owners[_tokenId] = _to;
        
        emit Transfer(_from, _to, _tokenId);
        
    }
    
    function _clearApproval(uint256 _tokenId) private {
        if(_approvals[_tokenId] != address(0)){ _approvals[_tokenId] = address(0); }
    }
    
    function transferFrom(address _from, address _to,  uint256 _tokenId) public {
        _transferFrom(_from, _to, _tokenId);
    }
    
    
    function _mint(address _to, uint256 _tokenId) private {
        require(_to != address(0), "Unable to mint to ZERO address");
        require(!_existsToken(_tokenId), "Already minted token.");

        uint256 n = _counts[_to];
        require(n + 1 > n, "The number of tokens for '_to' has overflowed.");
        _counts[_to] = n + 1;
        
        _owners[_tokenId] = _to;

        emit Transfer(address(0), _to, _tokenId);
    }
    
    function mint(address _to, uint256 _tokenId) public {
        require(msg.sender == _admin, "Only admin can mint.");
        
        _mint(_to, _tokenId);
    }
    
    
    
    function supportsInterface(bytes4 _interfaceId) public view returns(bool){
        return _supportedInterfaces[_interfaceId];
        
    }
    
    function _registerInterface(bytes4 _interfaceId) internal{
        
        require(_interfaceId != 0xffffffff, "Invalid interface id : 0xffffffff");
        _supportedInterfaces[_interfaceId] = true;
        
    }
    
    
}