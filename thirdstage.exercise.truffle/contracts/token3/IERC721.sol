pragma solidity ^0.5.0;

import "../../node_modules/@openzeppelin/contracts/drafts/Counters.sol";

/// @title EIP 721 Non-Fungible Token Standard
/// @dev refer https://eips.ethereum.org/EIPS/eip-721
/// @dev refer https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v2.5.0/contracts/token/ERC721/IERC721.sol
contract IERC721{
    
    event Transfer(address indexed _from, address indexed _to, uint256 indexed _tokenId);
    
    event Approval(address indexed _owner, address indexed _approved, uint256 indexed _tokenId);
    
    event ApprovalForAll(address indexed _owner, address indexed _operator, bool _approved);
    
    
    /// @notice Count all NFTs of the specified owner
    function balanceOf(address _onwer) public view returns (uint256);
    
    
    function onwerOf(uint256 _tokenId) public view returns (address);
    
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId) public;
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes memory data) public;
    
    function transferFrom(address _from, address _to, uint256 _tokenId) public;
    
    function approve(address _approved, uint256 _tokenId) public;
    
    function setApprovalForAll(address _operator, bool _approved) public; //toggling
    
    function getApproved(uint256 _tokenId) public view returns (address);
    
    function isApprovedForAll(address _owner, address _operator) public view returns (bool);
    
    
    function supportsInterface(bytes4 interfaceId) public view returns (bool);
    
    
    
    
}