
// https://github.com/trufflesuite/truffle/tree/v5.1.5/packages/hdwallet-provider
// https://iancoleman.io/bip39/
const HDWalletProvider = require("@truffle/hdwallet-provider");

const fs = require('fs');
const ganacheConfig = fs.readFileSync('scripts/ganache-cli.properties').toString();
const ganacheNetVer = ganacheConfig.match(/ethereum.netVersion=[0-9]*/g)[0].substring(20);
const ganacheHost =  ganacheConfig.match(/ethereum.host=.*/g)[0].substring(14);
const ganachePort =  ganacheConfig.match(/ethereum.port=[0-9]*/g)[0].substring(14);
const ganacheFrom =  ganacheConfig.match(/ethereum.from=.*/g)[0].substring(14);

const privateKeys = [
    "020c5a0c4f9ac0b3d88b6f18f4b8207416cac31bb7d45e9b344e292bdcc704d0", //0x050A32F6172988ECAe7cCc74779C6b1728EAaE44
    "731f28cf9f28eeb2b13b15b3c96e8f4ec74345af5ee51fd16632565588429f3f", //0xa76B9BFbfc081800F49C096f5bE30c076041701e
    "d9d1592d14407aa9bdcafdd1e3430cde1223ae56871d16edd4d5d209aca5c576", //0x418DB1D5Fe323299DeFDE67f7DCa6a2A016c659b
    "c6340d85e32fd9b91254b53efea1b5ab49904f3797054fa7441387ef62cde114", //0xf535c2B3A2C6c0999067B8Aad72aE5BAD774F5b3
    "26f5816bd2d2edcff0e89695dc9ac05ef1ca8d5941132b0327a537879e83d6a7", //0xe3561C201eAE61271b99Ab17e33804Fc1A48e257
    "79999a765060abafb3c9b93ebd6000da16e1dac64221ff3ea9311dbd1851734f"  //0xaCEB106d13Ada89057C0FC41f6AbaA3Ad78265DC
];

module.exports = {
  // http://truffleframework.com/docs/advanced/configuration
  networks: {
    development: {
      host: ganacheHost,
      port: ganachePort,
      network_id: '*',
      from: ganacheFrom,
      gas: 0x10000000,
      gasPrice: 0
    },

    //GitHub : https://github.com/ethereum/ropsten/
    //Explorer : https://ropsten.etherscan.io/
    //Faucet : https://faucet.ropsten.be/
    ropsten: {
      provider: () => new HDWalletProvider(process.env.BIP39_MNEMONIC,"https://ropsten.infura.io/v3/" + process.env.INFURA_PROJECT_ID),
      network_id: '3',
      gas: 7E6,
      gasPrice: 1E10
    },

    //Explorer : https://rinkeby.etherscan.io/
    //Faucet : https://faucet.rinkeby.io/
    rinkeby: {
      provider: () => new HDWalletProvider(process.env.BIP39_MNEMONIC, "https://rinkeby.infura.io/v3/" + process.env.INFURA_PROJECT_ID),
      network_id: '4',
    },

    //GitHub : https://github.com/kovan-testnet/
    //Explorer : https://kovan.etherscan.io/
    //Faucet : https://faucet.kovan.network/
    kovan: {
      provider: () => new HDWalletProvider(process.env.BIP39_MNEMONIC, "https://kovan.infura.io/v3/" + process.env.INFURA_PROJECT_ID),
      network_id: '42', //https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version
      gas: 7E6,
      gasPrice: 5E10
    },

    chainztest: {
      // https://github.com/trufflesuite/truffle-hdwallet-provider#private-keys
      // https://github.com/trufflesuite/truffle/issues/1022
      provider: () => new HDWalletProvider(privateKeys, "http://besutest.chainz.network/", 0, 3),
      network_id: '2020',
      gas: 1E7,
      gasPrice: 0
    }
  },

  compilers: {
    solc: {
      version: "^0.5.0",
      optimizer: {
        enabled: true,
        runs: 200
      }
    }
  },

  mocha: {
    useColors: true,
    enableTimeouts: true,
    timeout: 180000
  }
}
