const MetaCoin = artifacts.require("MetaCoin");

//http://truffleframework.com/docs/getting_started/javascript-tests
contract('MetaCoin', function(accounts){
   
   it("should send coin correctly", async function(){

      let acc1 = accounts[0]; //sender
      let acc2 = accounts[1]; //receiver
      let amt = 10;
      let coin = await MetaCoin.deployed();
      
      let bal1From = (await coin.getBalance(acc1)).toNumber();
      let bal2From = (await coin.getBalance(acc2)).toNumber();
      
      await coin.sendCoin(acc2, amt, {from: acc1});
      
      let bal1To = (await coin.getBalance(acc1)).toNumber();
      let bal2To = (await coin.getBalance(acc2)).toNumber();
      
      assert.equal(bal1To, bal1From - amt, "Amount wasn't correctly taken from the sender.");
      assert.equal(bal2To, bal2From + amt, "Amount wasn't correctly sent to the receiver.");
   });
});