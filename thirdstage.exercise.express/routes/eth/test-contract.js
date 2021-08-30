const fs = require('fs');
const router = require("express-promise-router")();
const config = require('config');
const Web3 = require('web3');

const { abi } = require('@3rdstage/smart-contracts/build/truffle/Web3TestContract.json');
const web3 = new Web3(new Web3.providers.HttpProvider(config.get('web3.url'))); 

router.get('/test-contract', async (req, res) => {

  console.log(abi);
  console.log(config.get('smartContracts.solidityTestContract.addresses.kovan'));
  
  res.send(abi);
});

module.exports = router;
