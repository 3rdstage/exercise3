pragma solidity ^0.5.0;

import "./IERC721.sol";

import "../../node_modules/@openzeppelin/contracts/math/SafeMath.sol";
//import "../../node_modules/@openzeppelin/contracts/utils/Address.sol";
import "../../node_modules/@openzeppelin/contracts/drafts/Counters.sol";
import "../../node_modules/@openzeppelin/contracts/introspection/ERC165.sol";

/// @dev refer https://eips.ethereum.org/EIPS/eip-721
/// @dev refer https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v2.5.1/contracts/token/ERC721/ERC721.sol
contract ERC721 is IERC721, ERC165{
  using SafeMath for uint256;
  //using Address for address;
  using Counters for Counters.Counter;

  bytes4 private constant _ERC721_RECEIVED = 0x150b7a02;

  // token(ID) -> owner(address) map
  mapping(uint256 => address) private _owners;

  // token -> approved delegator map
  mapping(uint256 => address) private _approvals;

  // owner -> # of tokens map
  mapping(address => Counters.Counter) private _counts;  //derived

  // owner -> approver -> yes/no
  mapping(address => mapping(address => bool)) private _operators;

  bytes4 private constant _INTERFACE_ID_ERC721 = 0x80ac58cd;

  constructor() public{
    _registerInterface(_INTERFACE_ID_ERC721);
  }

  /// @dev override
  function balanceOf(address _owner) public view returns(uint256){
    require(_owner != address(0), "");

    return _counts[_owner].current();
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
    require(ownerOf(_tokenId) == _from, "The specified from address is not the owner of the specified token.");

    _clearApproval(_tokenId);

    _counts[_from].decrement();
    _counts[_to].increment();

    _owners[_tokenId] = _to;

    emit Transfer(_from, _to, _tokenId);

  }

  function _clearApproval(uint256 _tokenId) private {
    if(_approvals[_tokenId] != address(0)){ _approvals[_tokenId] = address(0); }
  }

  function _safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes memory _data) internal{
    _transferFrom(_from, _to, _tokenId);
    require(_checkOnERC721Received(_from, _to, _tokenId, _data), "Transfer to non ERC721Receiver implementer");
  }

  function _checkOnERC721Received(address _from, address _to, uint256 _tokenId, bytes memory _data) internal returns (bool){
    //if(!_to.isContract()){ return true; }

    //@TODO

    return false;
  }

  function transferFrom(address _from, address _to, uint256 _tokenId) public {
    require(_isApprovedOrOwner(msg.sender, _tokenId), "Caller is required to be owner, apporval or operator of the token.");
    _transferFrom(_from, _to, _tokenId);
  }

  function safeTransferFrom(address _from, address _to, uint256 _tokenId) public {
    safeTransferFrom(_from, _to, _tokenId, "");
  }

  function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes memory _data) public {
    require(_isApprovedOrOwner(msg.sender, _tokenId), "Caller is required to be owner, approval or operator of the token.");
    _safeTransferFrom(_from, _to, _tokenId, _data);
  }

  function _mint(address _to, uint256 _tokenId) internal {
    // https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v2.5.1/contracts/token/ERC721/ERC721.sol#L258
  }




}
