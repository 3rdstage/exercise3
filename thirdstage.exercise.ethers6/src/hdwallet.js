const ethers = require("ethers");
const fs = require('fs');

(async() => {
  const parentWallet = ethers.HDNodeWallet.fromPhrase(process.env.BIP39_MNEMONIC, '', "m/44'/60'/0'/0");
  const wallets = [];
  const n = 10;

  console.log(`Generating ${n} accounts from mnemonic`);
  for(let i = 0; i < n; i++){
    wallets.push(parentWallet.derivePath(`${i}`));

    console.log(`Wallet ${i}th: ${wallets[i].address}, ${wallets[i].path}`);
  }

})();
