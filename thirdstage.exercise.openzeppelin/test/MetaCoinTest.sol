pragma solidity ^0.4.2;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/MetaCoin.sol";


contract MetaCoinTest{

  function testInitialBalanceUsingDeployedContract(){

    MetaCoin coin = MetaCoin(DeployedAddresses.MetaCoin());

    uint expected = 10000;

    Assert.equal(coin.getBalance(tx.origin), expected, "Owner should have 10000 MetaCoin initially.");
  }

  function testInitialBalanceUsingNewMetaCoin(){
    MetaCoin coin = new MetaCoin();

    uint expected = 10000;

    Assert.equal(coin.getBalance(tx.origin), expected, "Owner should have 10000 MetaCoin initially.");
  }
}