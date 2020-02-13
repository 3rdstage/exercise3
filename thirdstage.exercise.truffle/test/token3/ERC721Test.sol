pragma solidity ^0.5.0;

import "truffle/Assert.sol";
import "../../contracts/token3/ERC721.sol";

contract ERC721Test{

    address addr1 = 0xC5776C5d4ba76dD38424A160927c6B7054b55edD;
    address addr2 = 0x99322780C19B664e9902Ff1031549da575De8F3B;
    address addr3 = 0xf0f0717dB9387ea3B095dE1FF43786C63DC93e45;
    
    function testMint1() public{

        ERC721 token = new ERC721();
        
        token.mint(addr1, 1);
        token.mint(addr2, 100);
        
        Assert.equal(token.ownerOf(1), addr1, "Minting or owner access method has defect.");
        Assert.equal(token.ownerOf(100), addr2, "Minting or owner access method has defect.");
    }
    
    function testBalanceOf1() public{
        
        ERC721 token = new ERC721();
        
        for(uint256 i = 1; i < 11; i++){ token.mint(addr1, i); }
        
        Assert.equal(token.balanceOf(addr1), 10, "ERC721.balanceOf behaves unexpectedly.");
        Assert.equal(token.balanceOf(addr2), 0, "ERC721.balanceOf behaves unexpectedly.");
    }
    
    function testApprove1() public{

        ERC721 token = new ERC721();
        
        token.mint(address(this), 1);
        token.mint(address(this), 100);

        token.approve(addr2, 1);
        
        Assert.equal(token.getApproved(1), address(this), "ERC721.sol: 'approve' or 'getApproved' behaves unexpectedly.");
        Assert.equal(token.getApproved(100), address(this), "ERC721.sol: 'approve' or 'getApproved' behaves unexpectedly.");
    }
    
    
    function testSetApprovalForAll1() public{

        ERC721 token = new ERC721();
        
        //token.mint(msg.sender, 1);
        //token.mint(msg.sender, 100);
        token.mint(address(this), 1);
        token.mint(address(this), 100);

        Assert.isFalse(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' behaves unexpectedly.");
        Assert.isFalse(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' behaves unexpectedly.");
        
        // set addr1 to operartor of this address
        token.setApprovalForAll(addr1, true);
        
        Assert.isTrue(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isFalse(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        
        // unset addr1 to operator of this address
        token.setApprovalForAll(addr1, false);
        
        Assert.isFalse(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isFalse(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        
        // set add2 to operator of this address
        token.setApprovalForAll(addr2, true);

        Assert.isFalse(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isTrue(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");

        // unset addr2 to opertor of this address
        token.setApprovalForAll(addr2, false);
        
        Assert.isFalse(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isFalse(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");

        // set both addr1 and addr2 to operator for this address
        token.setApprovalForAll(addr2, true);
        token.setApprovalForAll(addr1, true);
        
        Assert.isTrue(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isTrue(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        
        // unset both addr1 and addr2 from operators for this address
        token.setApprovalForAll(addr2, false);
        token.setApprovalForAll(addr1, false);
        
        Assert.isFalse(token.isApprovedForAll(address(this), addr1), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");
        Assert.isFalse(token.isApprovedForAll(address(this), addr2), "ERC721.sol: 'isApprovedForAll' or 'setApprovalForAll' behaves unexpectedly.");

    }
    
    
    function testTransferFromIn1() public{
        
        ERC721 token = new ERC721();
        
        token.mint(address(this), 1);
        token.approve(addr1, 1);
        token.mint(address(this), 100);

        Assert.equal(token.balanceOf(address(this)), 2, "ERC721.sol: 'mint' or 'balanceOf' hehaves unexpectedly.");
        Assert.equal(token.getApproved(1), addr1, "ERC721.sol: 'approve' or 'getApproved' behaves unexpectedly.");
        Assert.equal(token.balanceOf(addr2), 0, "ERC721.sol: 'mint' or 'balanceOf' hehaves unexpectedly.");
        
        // transfer token 1 to addr1
        token.transferFrom(address(this), addr2, 1);

        Assert.equal(token.balanceOf(address(this)), 1, "ERC721.sol: 'mint' or 'balanceOf' hehaves unexpectedly.");
        // 'transferFrom' remove exisiting approved for the transferred token
        Assert.equal(token.getApproved(1), address(0), "ERC721.sol: 'approve' or 'getApproved' behaves unexpectedly.");
        Assert.equal(token.balanceOf(addr2), 1, "ERC721.sol: 'mint' or 'balanceOf' hehaves unexpectedly.");
    }

}
