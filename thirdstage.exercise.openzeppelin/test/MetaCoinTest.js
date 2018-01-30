var MetaCoin = artifacts.require("MetaCoin");

// http://truffleframework.com/docs/getting_started/javascript-tests
contract("MetaCoin", function(accounts){

  it("should put 10000 MetaCoin into the 1st account.", function(){
    return MetaCoin.deployed().then(function(instance){
      return instance.getBalance.call(accounts[0]);
    }).then(function(balance){
      assert.equal(balance.valueOf(), 10000, "10000 wasn't in the 1st account");
    });
  });

  it("shoud send coin correctly.", function(){
     
     var meta;
     var acc1 = accounts[0];
     var acc2 = accounts[1];
     
     var acc1Balance0;
     var acc1Balance1;
     var acc1Balance0;
     var acc1Balance1;
     
     var amt = 10;
     
     return MetaCoin.deployed().then(function(instance){
        meta = instance;
        
        //balances before transfer
        acc1Balance0 = instance.getBalance.call(acc1);
        acc2Balance0 = instance.getBalance.call(acc2);
     }).then(function(){
        return meta.sendCoin(acc2, amt, {from: acc1});
     }).then(function(){
        //balances after transfer
        acc1Balance1 = instance.getBalance.call(acc1);
        acc2Balance1 = instance.getBalance.call(acc2);
     }).then(function(){
        assert.equal(acc1Balance1, acc1Balance0 - amt, "Amount wasn't correctly taken from the sender.");
        assert.equal(acc2Balance1, acc2Balance0 + amt, "Amount wasn't correctly sent to the receiver.");
     });
  });
});