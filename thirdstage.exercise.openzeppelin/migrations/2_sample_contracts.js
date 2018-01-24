var BareCoin = artifacts.require("./BareCoin.sol");

module.exports = function(deployer) {
  deployer.deploy(BareCoin);
};