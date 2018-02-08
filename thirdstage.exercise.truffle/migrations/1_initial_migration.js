var Migrations = artifacts.require("./Migrations.sol");
// var BareCoin = artifacts.require("BareCoin");

module.exports = function(deployer) {
  deployer.deploy(Migrations);
  //deployer.deploy(BareCoin);
};
