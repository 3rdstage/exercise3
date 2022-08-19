
// https://dev.to/yakult/02-understanding-blockchain-with-ethersjs-4-tasks-of-wallet-nn5

const ethers = require('ethers');

const mnemonic = 'myth like bonus scare over problem client lizard pioneer submit female collect';
ethers.utils.isValidMnemonic(mnemonic);

const hdnode = ethers.utils.HDNode.fromMnemonic(mnemonic);
const basePath : string = ethers.utils.defaultPath;

console.log(`Default Path : ${ethers.utils.defaultPath}`);
console.log(`Base Path: ${basePath}`);
console.log(`Current Path : ${hdnode.path}`);

const wallet = ethers.Wallet.fromMnemonic(mnemonic);

console.log('End of Exercise');

