//require("babel-polyfill");
//require("babel-register")({
//  "presets": ["env"],
//  "plugins": ["syntax-async-functions","transform-regenerator"]
//});

// https://github.com/trufflesuite/truffle/tree/v5.1.5/packages/hdwallet-provider
// https://iancoleman.io/bip39/
const HDWalletProvider = require("@truffle/hdwallet-provider");
const mnemonic = "unit ramp wire absent film fox arrest govern ball make sunny fork"; // should be 12 words

const fs = require('fs');
const ganacheConfig = fs.readFileSync('scripts/ganache-cli.properties').toString();
const ganacheNetVer = ganacheConfig.match(/ethereum.netVersion=[0-9]*/g)[0].substring(20);
const ganacheHost =  ganacheConfig.match(/ethereum.host=.*/g)[0].substring(14);
const ganachePort =  ganacheConfig.match(/ethereum.port=[0-9]*/g)[0].substring(14);
const ganacheFrom =  ganacheConfig.match(/ethereum.from=.*/g)[0].substring(14);

module.exports = {
   // http://truffleframework.com/docs/advanced/configuration
    networks: {
      development: {
        host: ganacheHost,
        port: ganachePort,
        network_id: ganacheNetVer,
        from: ganacheFrom,
        gasPrice: 0,
        gas: 0x10000000
      },

      ropsten: {
        provider: () => new HDWalletProvider(mnemonic,"https://ropsten.infura.io/v3/ec117ed9ca2841f2a5ec87f225c2629e"),
        network_id: '3',
      },

      rinkeby: {
        provider: function(){
          return new HDWalletProvider(mnemonic, "https://rinkeby.infura.io/v3/ec117ed9ca2841f2a5ec87f225c2629e");
        },
        network_id: 4,
      },

      kovan: {
        provider: function(){
          return new HDWalletProvider(mnemonic, "https://kovan.infura.io/v3/ec117ed9ca2841f2a5ec87f225c2629e");
        },
        network_id: 6,
      },
    },

    compilers: {
      solc: {
        version: "^0.4.0 || ^0.5.0",
        optimizer: {
          enabled: true,
          runs: 200
        }
      }
    }
};
