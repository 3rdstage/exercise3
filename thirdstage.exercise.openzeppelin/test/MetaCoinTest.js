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

});