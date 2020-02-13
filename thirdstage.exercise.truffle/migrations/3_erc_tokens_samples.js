var ERC721Enumerable = artifacts.require("token3/ERC721Enumerable");

module.exports = function(deployer) {
  deployer.deploy(ERC721Enumerable);
};