require("babel-polyfill");
require("babel-register")({
  "presets": ["env"],
  "plugins": ["syntax-async-functions","transform-regenerator"]
});

const HDWalletProvider = require("truffle-hdwallet-provider");
const mnemonic = "hell bent for leather";

const fs = require('fs');

const accounts = fs.readFileSync('run/accounts').toString().split('\n');
const config = fs.readFileSync('scripts/quorum/solo/config.sh').toString();
const rpcPort = config.match(/\[rpcport\]=[0-9]*/g)[0].substring(10);
const networkId = config.match(/\[networkid\]=[0-9]*/g)[0].substring(12);

module.exports = {
   // http://truffleframework.com/docs/advanced/configuration
    networks: {
      development: {
        host: "192.168.56.101",
        port: rpcPort,
        network_id: networkId,
        from: accounts[0],
        gasPrice: 0,
        gas: 0x10000000
      }
    },
    
    ropsten: {
      provider: function(){
        return new HDWalletProvider(mnemonic, "https://ropsten.infura.io/Sqj6qg9ix47UK1EBQQb0");
      },
      network_id: 3,
    }
    
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
