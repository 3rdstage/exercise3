
//import {ethers} from "ethers";
const ethers = require("ethers");

module.exports = {
  logBlockHeight: logBlockHeight
};

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);

function logBlockHeight(reqParams, context, ee, next){
  eth.getBlockNumber().then(n => console.log(`Ethereum Mainnet Block Height: ${n}`));

  return next();
}

