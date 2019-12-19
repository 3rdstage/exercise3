//require("babel-polyfill");
//require("babel-register")({
//  "presets": ["env"],
//  "plugins": ["syntax-async-functions","transform-regenerator"]
//});

const HDWalletProvider = require("@truffle/hdwallet-provider");
const mnemonic = "hell bent for leather";

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
      }
    },

    ropsten: {
      provider: function(){
        return new HDWalletProvider(mnemonic, "https://ropsten.infura.io/Sqj6qg9ix47UK1EBQQb0");
      },
      network_id: 3,
    },

    rinkeby: {
      provider: function(){
        return new HDWalletProvider(mnemonic, "https://rinkeby.infura.io/Sqj6qg9ix47UK1EBQQb0");
      },
      network_id: 4,
    },

    kovan: {
      provider: function(){
        return new HDWalletProvider(mnemonic, "https://kovan.infura.io/Sqj6qg9ix47UK1EBQQb0");
      },
      network_id: 6,
    },

    solc: {
      optimizer: {
        enabled: true,
        runs: 200
      }
    }
};
