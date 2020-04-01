
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
  "ae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f",
  "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3",
  "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63"
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
