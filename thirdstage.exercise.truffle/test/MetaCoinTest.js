var MetaCoin = artifacts.require("MetaCoin");

// http://truffleframework.com/docs/getting_started/javascript-tests
contract("MetaCoin", function(accounts){
   
  it("shoud send coin correctly.", function(){

     var acc1 = accounts[0]; //sender
     var acc2 = accounts[1]; //receiver
     var amt = 10;
     var coin;
     
     var bal1From, bal1To, bal2From, bal2To;
     
     return MetaCoin.deployed().then(function(instance){
        coin = instance;
        return coin.getBalance(acc1);
     }).then(function(bal){
        bal1From = bal.toNumber();
        return coin.getBalance(acc2);
     }).then(function(bal){
        bal2From = bal.toNumber();
        return coin.sendCoin(acc2, amt, {from: acc1});
     }).then(function(){
        return coin.getBalance(acc1);
     }).then(function(bal){
        bal1To = bal.toNumber();
        return coin.getBalance(acc2);
     }).then(function(bal){
        bal2To = bal.toNumber();

        assert.equal(bal1To, bal1From - amt, "Amount wasn't correctly taken from the sender.");
        assert.equal(bal2To, bal2From + amt, "Amount wasn't correctly sent to the receiver.");
        
     });
  });
  
});