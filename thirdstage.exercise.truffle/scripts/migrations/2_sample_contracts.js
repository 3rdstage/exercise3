var BareCoin = artifacts.require("BareCoin");
var MetaCoin = artifacts.require("MetaCoin");
var GlobalSessionRepo = artifacts.require("GlobalSessionRepo");
var StringUtils = artifacts.require("StringUtils");
var Bytes32Utils = artifacts.require("Bytes32Utils")


module.exports = function(deployer) {
  deployer.deploy(BareCoin);
  deployer.deploy(MetaCoin);
  deployer.deploy(GlobalSessionRepo);

};