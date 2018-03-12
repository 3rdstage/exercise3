const MetaCoin = artifacts.require("MetaCoin");

contract('MetaCoin', function(accounts){
   
   it("should put 10,000 MetaCoin in the first account", async function(){
      let instance = await MetaCoin.deployed();
      let balance = await instance.getBalance.call(accounts[0]);
      assert.equal(balance.valueOf(), 10000);
   });
   
   it("should send coin correctly", async function(){
      let acc1 = accounts[0];
      let acc2 = accounts[1];
      let amt = 10;
      let instance = await MetaCoin.deployed();
      
      let bal1From = (await instance.getBalance.call(acc1)).toNumber();
      console.log(bal1From);
      let bal2From = (await instance.getBalance.call(acc2)).toNumber();
      console.log(bal2From);
      
      await instance.sendCoin(acc2, amt, {from: acc1});
      
      let bal1To = (await instance.getBalance.call(acc1)).toNumber();
      console.log(bal1To);
      let bal2To = (await instance.getBalance.call(acc2)).toNumber();
      console.log(bal2To);
      
      assert.equal(bal1To, bal1From - amt, "Amount wasn't correctly taken from the sender.");
      assert.equal(bal2To, bal2From + amt, "Amount wasn't correctly sent to the receiver.");
      
   });
});