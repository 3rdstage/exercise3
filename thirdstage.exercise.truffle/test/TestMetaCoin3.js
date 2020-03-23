var fs = require('fs');
var Web3 = require("web3");
var provider = new Web3.providers.HttpProvider("http://192.168.56.101:8545");
var web3 = new Web3(provider);

contract('MetaCoin', function(accounts){
   
   var coinAbiFile = fs.readFileSync('test/MetaCoin.abi');
   var coinAbi = JSON.parse(coinAbiFile.toString());
   var coinContract = web3.eth.contract(coinAbi);
    
   it("shoud send coin correctly.", function(){
      
      var acc1 = web3.eth.accounts[0]; //sender
      var acc2 = web3.eth.accounts[1]; //receiver
      var amt = 10;

      var coin = coinContract.at("0x8007ba85a0cff8267d9602a1a8428d9725e9f4c4");
      
      var bal1From = coin.getBalance.call(acc1).toNumber();
      var bal2From = coin.getBalance.call(acc2).toNumber();
      
      coin.sendCoin(acc2, amt, {from: acc1});
      
      var bal1To = coin.getBalance.call(acc1).toNumber();
      var bal2To = coin.getBalance.call(acc2).toNumber();
      
      assert.equal(bal1To, bal1From - amt, "Amount wasn't correctly taken from the sender.");
      assert.equal(bal2To, bal2From + amt, "Amount wasn't correctly sent to the receiver.");
   });
   
});

