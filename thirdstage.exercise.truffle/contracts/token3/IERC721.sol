pragma solidity ^0.5.0;

import "../../node_modules/@openzeppelin/contracts/drafts/Counters.sol";

/// @title EIP 721 Non-Fungible Token Standard
/// https://eips.ethereum.org/EIPS/eip-721
interface IERC721{
    
    event Transfer(address indexed _from, address indexed _to, uint256 indexed _tokenId);
    
    event Approval(address indexed _owner, address indexed _approved, uint256 indexed _tokenId);
    
    event ApprovalForAll(address indexed _owner, address indexed _operator, bool _approved);
    
    
    /// @notice Count all NFTs of the specified owner
    function balanceOf(address _onwer) external view returns (uint256);
    
    
    function onwerOf(uint256 _tokenId) external view returns (address);
    
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId) external payable;
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes calldata data) external payable;
    
    function transferFrom(address _from, address _to, uint256 _tokenId) external payable;
    
    function approve(address _approved, uint256 _tokenId) external payable;
    
    function setApprovalForAll(address _operator, bool _approved) external; //toggling
    
    function getApproved(uint256 _tokenId) external view returns (address);
    
    function isApprovedForAll(address _owner, address _operator) external view returns (bool);
    
    
    function supportsInterface(bytes4 interfaceId) external view returns (bool);
    
    
    
    
}