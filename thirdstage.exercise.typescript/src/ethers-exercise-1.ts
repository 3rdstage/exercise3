
// https://dev.to/yakult/02-understanding-blockchain-with-ethersjs-4-tasks-of-wallet-nn5

const ethers = require('ethers');

const mnemonic = 'myth like bonus scare over problem client lizard pioneer submit female collect';
ethers.utils.isValidMnemonic(mnemonic);

const hdnode = ethers.utils.HDNode.fromMnemonic(mnemonic);
const basePathPrefix : string = ethers.utils.defaultPath.slice(0, -1);

console.log(`Default Path : ${ethers.utils.defaultPath}`);
console.log(`Base Path Prefix: ${basePathPrefix}`);
console.log(`Current Path : ${hdnode.path}`);

const wallet0 = ethers.Wallet.fromMnemonic(mnemonic);
const wallets = [];

for(let i = 0; i < 20; i++){
  
}


console.log('End of Exercise');

