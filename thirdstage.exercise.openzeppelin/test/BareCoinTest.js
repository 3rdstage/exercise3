var BareCoin = artifacts.require("BareCoin")

contract("BareCoin", function(accounts){
   
   it("should mint 1000", function(){
      return BareCoin.deployed().then(function(instance){
         
         return instance.mint.call(accounts[0], 1000);
         
      }); 
      
   });
   
   
})