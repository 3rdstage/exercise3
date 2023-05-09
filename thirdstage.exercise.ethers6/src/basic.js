
//import {ethers} from "ethers";
const ethers = require("ethers");

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);

eth.getBlockNumber().then(n => console.log(`Ethereum Mainnet Block Height: ${n}`));

