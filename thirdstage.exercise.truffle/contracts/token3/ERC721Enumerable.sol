pragma solidity ^0.5.0;

import "./ERC721.sol";

contract ERC721Enumerable is ERC721{

    uint256[] private _tokens;   // all tokens by sequence;

    mapping(uint256 => uint256) private _tokensIndex;

    mapping(address => uint256[]) private _ownerTokensMap;


    function totalSupply() public view returns(uint256){
        return _tokens.length;
    }

    function tokenByIndex(uint256 _index) public view returns (uint256 _tokenId, bool _hasMore) {
        require(_index < totalSupply(), "ERC721Enumerable: Index out of bounds.");

        _tokenId = _tokens[_index];
        _hasMore = ( _index != totalSupply() - 1);
    }

    // @dev Overrid
    function _mint(address _to, uint256 _tokenId) internal {
        super._mint(_to, _tokenId);

        _tokensIndex[_tokenId] = _tokens.length;
        _tokens.push(_tokenId); 
    }


}
