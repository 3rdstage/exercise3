var BareCoin = artifacts.require("./BareCoin.sol");
var MetaCoin = artifacts.require("./MetaCoin.sol");

module.exports = function(deployer) {
  deployer.deploy(BareCoin);
  deployer.deploy(MetaCoin);
};