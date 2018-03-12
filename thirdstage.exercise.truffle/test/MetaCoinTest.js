var MetaCoin = artifacts.require("MetaCoin");

// http://truffleframework.com/docs/getting_started/javascript-tests
contract("MetaCoin", function(accounts){
   
   console.log(accounts);

  it("should put 10000 MetaCoin into the 1st account.", function(){
     
    console.log(accounts);
    
    return MetaCoin.deployed().then(function(instance){
      return instance.getBalance.call(accounts[0]);
    }).then(function(balance){
      assert.equal(balance.valueOf(), 10000, "10000 wasn't in the 1st account");
    });  
  });

  it("shoud send coin correctly.", function(){
     
     var meta;
     var acc1 = accounts[0]; //sender
     var acc2 = accounts[1]; //receiver
     
     var amt = 10;
     var bal1From;
     var bal1To;
     var bal2From;
     var bal2To;
     var b
     
     return MetaCoin.deployed().then(function(instance){
        meta = instance;
        return meta.getBalance.call(acc1);
        
     }).then(function(bal){
        bal1From = bal.toNumber();
        
        return meta.getBalance.call(acc2);
     }).then(function(bal){
        bal2From = bal.toNumber();
        return meta.sendCoin(acc2, amt, {from: acc1});
     }).then(function(){
        return meta.getBalance.call(acc1);
     }).then(function(bal){
        bal1To = bal.toNumber();
        return meta.getBalance.call(acc2);
     }).then(function(bal){
        bal2To = bal.toNumber();

        assert.equal(bal1To, bal1From - amt, "Amount wasn't correctly taken from the sender.");
        assert.equal(bal2To, bal2From + amt, "Amount wasn't correctly sent to the receiver.");
        
     });
  });
  
});