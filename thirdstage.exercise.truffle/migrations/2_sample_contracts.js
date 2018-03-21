var BareCoin = artifacts.require("./BareCoin.sol");
var MetaCoin = artifacts.require("./MetaCoin.sol");
var StringUtils = artifacts.require("./StringUtils.sol");

module.exports = function(deployer) {
  deployer.deploy(BareCoin);
  deployer.deploy(MetaCoin);
};