var BareCoin = artifacts.require("./BareCoin.sol");

module.exports = function(deployer) {
  // deployment steps
  deployer.deploy(BareCoin);
};