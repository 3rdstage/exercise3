
module.exports = function(callback) {

	web3.eth.getAccounts().then(accts => console.log(accts));

}
