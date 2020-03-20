
module.exports = function(callback) {
  console.log("'web3.js' version (web3.version): " + web3.version);
  console.log("current client version (web3.eth.getNodeInfo): ");
  web3.eth.getNodeInfo().then(console.log);
}

