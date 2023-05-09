
//import {ethers} from "ethers";
const ethers = require("ethers");

module.exports = {
  logBlockHeight: logBlockHeight,
  getComptrollerCompAddress: getComptrollerCompAddress,
  getComptrollerMarketCount: getComptrollerMarketCount
};

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);

// Compound's `Comptroller` contract instance at Mainnet
// https://docs.compound.finance/v2/comptroller/
// https://etherscan.io/address/0x3d9819210A31b4961b30EF54bE2aeD79B9c9Cd3B
// https://github.com/compound-finance/compound-protocol/blob/v2.8.1/contracts/Comptroller.sol
const comptroller = {
  address : '0x3d9819210A31b4961b30EF54bE2aeD79B9c9Cd3B',
  abi : [
    "function getCompAddress() view returns (address)",
    "function getAllMarkets() view returns (address[] memory)",
    "function compRate() view returns (uint256)",
  ]
};

comptroller.contract = new ethers.Contract(
  comptroller.address, comptroller.abi, eth);

function logBlockHeight(reqParams, context, ee, next){
  eth.getBlockNumber().then(n => console.log(`Ethereum Mainnet Block Height: ${n}`));

  return next();
}

// https://docs.ethers.org/v6/getting-started/#starting-contracts
// Gets the address of the COMP token
async function getComptrollerCompAddress(reqParams, context, ee, next){

  const addr = await comptroller.contract.getCompAddress();
  console.log(`COMP Token Address: ${addr}`);

  return next();
}

function getComptrollerMarketCount(reqParams, context, ee, next){

  comptroller.contract.getAllMarkets().then(tokens => console.log(`Market Count: ${tokens.length}`));

  return next();
}


