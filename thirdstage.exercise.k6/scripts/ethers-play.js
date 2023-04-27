
const ethers = require('ethers');

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);


console.dir(Object.keys(ethers), {'maxArrayLength': null});
eth.getBlockNumber().then(n => console.log(n));