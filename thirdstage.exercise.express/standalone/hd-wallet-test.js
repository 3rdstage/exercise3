const { hdkey } = require('ethereumjs-wallet'); // https://www.npmjs.com/package/ethereumjs-wallet
const bip39 = require("bip39"); // https://www.npmjs.com/package/bip39

// https://dev.to/dongri/ethereum-wallet-sample-code-10o2
const mnemonic = 'forest unveil solar vapor innocent furnace finish logic fix foil record coast'
const parentKey = hdkey.fromMasterSeed(bip39.mnemonicToSeedSync(mnemonic)).derivePath("m/44'/60'/0'/0");

const wallets = [];
for(let i = 0; i < 10; i++){
  wallets.push(parentKey.deriveChild(i).getWallet());
}

console.log(`Mnemonic : ${mnemonic}`);
for(i in wallets){
  console.log(`  Account : ${i}th`)
  console.log(`    Private Key : ${wallets[i].getPrivateKeyString()}`);
  console.log(`    Address     : ${wallets[i].getChecksumAddressString()}`);
}
