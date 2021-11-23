const { hdkey } = require('ethereumjs-wallet'); // https://www.npmjs.com/package/ethereumjs-wallet
const bip39 = require("bip39"); // https://www.npmjs.com/package/bip39

// https://dev.to/dongri/ethereum-wallet-sample-code-10o2
const mnemonic = 'narrow fiber erode bomb moment cube culture year acquire lamp provide uncover sword domain video'
const rootKey = hdkey.fromMasterSeed(bip39.mnemonicToSeedSync(mnemonic));
const parentKey = rootKey.derivePath("m/44'/60'/0'/0");

const ethKeyPairs = {};
ethKeyPairs.menomic = mnemonic;
ethKeyPairs.baseKey = parentKey.getWallet().getPrivateKeyString();
ethKeyPairs.keyPairs = [];
for(let i = 0; i < 10; i++){
  let wallet = parentKey.deriveChild(i).getWallet();
  let keyPair = {};
  keyPair.privateKey = wallet.getPrivateKeyString();
  keyPair.publicKey = wallet.getPublicKeyString();
  keyPair.address = wallet.getChecksumAddressString();
  ethKeyPairs.keyPairs.push(keyPair);
}
console.log(JSON.stringify(ethKeyPairs, null, 2));


