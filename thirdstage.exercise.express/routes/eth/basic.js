//const express = require('express');
//const router = express.Router();
const router = require("express-promise-router")();
const config = require('config');
const Web3 = require('web3');

const web3 = new Web3(new Web3.providers.HttpProvider(config.get('web3.url'))); 

router.get('/basic', async (req, res) => {
  let basic = {
    url : config.get('web3.url'),
    web3 : {
      clientVersion: await web3.eth.getNodeInfo(),
      eth : {
        chainId: await web3.eth.getChainId(),
        protocolVersion: await web3.eth.getProtocolVersion(),
        blockHeight: await web3.eth.getBlockNumber(),
        hashrate: await web3.eth.getHashrate(),
        gasPrice: await web3.eth.getGasPrice()
      }
    }
  }
  
  res.header("Content-Type", "application/json");
  res.send(basic);
});

module.exports = router;