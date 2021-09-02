const fs = require('fs');
const config = require('config');
const router = require("express-promise-router")();
const request = require('request');

let web3;
let contract;

function init(_web3, _db){
  
  web3 = _web3;
  const abi = require('@3rdstage/smart-contracts/build/truffle/Web3TestContract.json').abi;
  const contractAddr = config.get('smartContracts.web3TestContract.addresses.kovan');

  contract = new web3.eth.Contract(abi, contractAddr);
}

router.get('/test-contract/months', async (req, res) => {

  console.log(contract.defaultAccount);  
  const months = await contract.methods.monthNames().call();
  
  res.header("Content-Type", "application/json");
  res.send(months);
});

router.get('/test-contract/sum', async (req, res) => {
  
  const a = req.query.a;
  const b = req.query.b;
  const sum = await contract.methods.sum(a, b).call() 

  res.header("Content-Type", "application/json");
  res.send({a: a, b: b, sum: sum});  
});

router.post('/test-contract/eth', async (req, res) => {
  
  const amt = req.query.wei;
  
  console.log(await web3.eth.defaultChain);
  console.log(config.testAccounts[1].address);
  console.log(await web3.eth.net.getId());
  console.log(await web3.eth.getGasPrice());

  const sign = await web3.eth.accounts.signTransaction({
    to : config.testAccounts[1].address,
    value: amt,
    gas: 2000000
  }, config.testAccounts[0].key);
  console.log(sign);  
  
  const tx = await web3.eth.sendSignedTransaction(sign.rawTransaction);

  res.header("Content-Type", "application/json");
  res.send(tx);  
});

router.post('/test-contract/counter', async (req, res) => {
  
  const func = contract.methods.countUp().encodeABI();
  //console.log(contract.options.address);
  
  const signed = await web3.eth.accounts.signTransaction({
    from : config.testAccounts[0].address,
    to: contract.options.address,
    gas: 2000000,
    data: func
  }, config.testAccounts[0].key);
  
  console.log(signed);
  
  const result = await web3.eth.sendSignedTransaction(signed.rawTransaction);
  
  res.header("Content-Type", "application/json");
  res.send(result);  
  
});


module.exports = (_web3, _db) => {
  init(_web3, _db);
  return router;
};
