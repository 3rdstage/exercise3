pragma solidity ^0.5.0;

import "truffle/Assert.sol";
import "../../contracts/token3/ERC721.sol";

contract ERC721Test{
    
    function testMint1() public{
        
        address addr1 = 0xC5776C5d4ba76dD38424A160927c6B7054b55edD;
        address addr2 = 0x99322780C19B664e9902Ff1031549da575De8F3B;
        
        ERC721 token = new ERC721();
        
        token.mint(addr1, 1);
        token.mint(addr2, 100);
        
        Assert.equal(token.ownerOf(1), addr1, "Minting or owner access method has defect.");
    }
    
    
}