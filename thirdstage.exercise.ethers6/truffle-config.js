// cspell:words websockets solcjs hdwallet

// https://github.com/trufflesuite/truffle/tree/v5.1.5/packages/hdwallet-provider
// https://web3js.readthedocs.io/en/v1.3.0/web3.html#providers
// https://iancoleman.io/bip39/
const HDWalletProvider = require("@truffle/hdwallet-provider");
const Web3HttpProvider = require('web3-providers-http');
const Web3WsProvider = require('web3-providers-ws');

// Read properties for local standalone Ganache CLI node
const mnemonic = process.env.BIP39_MNEMONIC;
const fs = require('fs');
const config = fs.readFileSync('scripts/ganache.properties').toString();
const ganache = {
  host : config.match(/ethereum.host=.*/g)[0].substring(14),
  port : config.match(/ethereum.port=[0-9]*/g)[0].substring(14),
  chain : config.match(/ethereum.chainId=[0-9]*/g)[0].substring(17),
  websocket: false
};

// https://www.npmjs.com/package/web3-providers-http
const defaultHttpOptions = {
  keepAlive: true, timeout: 70000
};

// https://www.npmjs.com/package/web3-providers-ws
// https://github.com/theturtle32/WebSocket-Node/blob/v1.0.31/docs/WebSocketClient.md#client-config-options
const defaultWsOptions = {
  timeout: 600000,

  clientConfig: {
    maxReceivedFrameSize: 100000000,
    maxReceivedMessageSize: 100000000,

    keepalive: true,
    keepaliveInterval: 60000,
  },

  reconnect: { auto: true, delay: 5000, maxAttempts: 5, onTimeout: false }
};

module.exports = {

  // https://www.trufflesuite.com/docs/truffle/reference/configuration#networks
  // https://github.com/trufflesuite/truffle/tree/develop/packages/hdwallet-provider#general-usage
  // https://www.npmjs.com/package/web3-providers-http
  // https://www.npmjs.com/package/web3-providers-ws
  // https://web3js.readthedocs.io/en/v1.7.0/web3.html#providers
  networks: {

    // https://www.trufflesuite.com/docs/truffle/reference/choosing-an-ethereum-client#truffle-develop
    builtin: {    // truffle built-in client : aka `truffle develop`
      host: '127.0.0.1',
      port: 9545,
      network_id: "*"
    },

    development: {
      host: ganache.host,
      port: ganache.port,
      network_id: ganache.chain,
      gas: 3E8,
      gasPrice: 0,
      websockets: ganache.websocket,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    mainnet: {
      provider: () => new HDWalletProvider(
        mnemonic, "https://mainnet.infura.io/v3/" + process.env.INFURA_PROJECT_ID),
      network_id: '1',
      skipDryRun: true,
      disableConfirmationListener: true
    },

    //Ropsten : PoW
    //GitHub : https://github.com/ethereum/ropsten/
    //Explorer : https://ropsten.etherscan.io/
    //Faucet : https://faucet.ropsten.be/
    ropsten: {
      provider: () => new HDWalletProvider(
        mnemonic, "https://ropsten.infura.io/v3/" + process.env.INFURA_PROJECT_ID),
      network_id: '3',
      gas: 7E6,
      gasPrice: 1E10,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    //Rinkeby : PoA
    //Explorer : https://rinkeby.etherscan.io/
    //Faucet : https://faucet.rinkeby.io/
    //Avg. Block Time : 15s
    rinkeby: {
      provider: () =>
        new HDWalletProvider({
          chainId: 4,
          mnemonic: mnemonic,
          providerOrUrl: new Web3HttpProvider(
            "https://rinkeby.infura.io/v3/" + process.env.INFURA_PROJECT_ID, defaultHttpOptions),
          pollingInterval: 5500
        }),
      network_id: '4',
      skipDryRun: true,
      disableConfirmationListener: true
    },

    rinkeby_ws: {
      provider: () => {
        // Monkey patch to support `web3.eth.subscribe()` function
        // https://github.com/trufflesuite/truffle/issues/2567
        const wsProvider = new Web3WsProvider(
          "wss://rinkeby.infura.io/ws/v3/" + process.env.INFURA_PROJECT_ID, defaultWsOptions);
        HDWalletProvider.prototype.on = wsProvider.on.bind(wsProvider);
        return new HDWalletProvider({
          mnemonic: mnemonic,
          providerOrUrl: wsProvider,
          pollingInterval: 5500
        });
      },
      network_id: '4', //https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version
      websockets: true,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    //Kovan : PoA
    //GitHub : https://github.com/kovan-testnet/
    //Explorer : https://kovan.etherscan.io/
    //Faucet : https://github.com/kovan-testnet/faucet
    //Avg. Block Time : 4s
    kovan: {
      provider: () =>
        new HDWalletProvider({
          mnemonic: mnemonic,
          providerOrUrl: new Web3HttpProvider(
            "https://kovan.infura.io/v3/" + process.env.INFURA_PROJECT_ID, defaultHttpOptions),
          pollingInterval: 2000
        }),
      network_id: '42', //https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version
      //gas: 7E6,
      //gasPrice: 5E10,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    kovan_ws: {
      provider: () => {
        // Monkey patch to support `web3.eth.subscribe()` function
        // https://github.com/trufflesuite/truffle/issues/2567
        const wsProvider = new Web3WsProvider(
          "wss://kovan.infura.io/ws/v3/" + process.env.INFURA_PROJECT_ID, defaultWsOptions);
        HDWalletProvider.prototype.on = wsProvider.on.bind(wsProvider);
        return new HDWalletProvider({
          mnemonic: mnemonic,
          providerOrUrl: wsProvider,
          pollingInterval: 2000
        });
      },
      network_id: '42', //https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version
      //gas: 7E6,
      //gasPrice: 5E10,
      websockets: true,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    // Goerli : PoA
    // GitHub : https://github.com/goerli/testnet
    // Explorer : https://goerli.etherscan.io/
    // Faucet :
    // Avg. Block Time : 15s
    goerli: {
      provider: () => new HDWalletProvider({
        mnemonic: mnemonic,
        providerOrUrl: new Web3HttpProvider(
          "https://goerli.infura.io/v3/" + process.env.INFURA_PROJECT_ID, defaultHttpOptions),
        pollingInterval: 15000
      }),
      network_id: '5',
      skipDryRun: true,
      disableConfirmationListener: true
    },

    // Polygon Testnet Mumbai
    // https://github.com/ethereum-lists/chains/blob/master/_data/chains/eip155-80001.json
    // https://mumbai.polygonscan.com/
    mumbai: {
      provider: () => new HDWalletProvider({
        mnemonic: mnemonic,
        providerOrUrl: new Web3HttpProvider('https://rpc-mumbai.maticvigil.com', defaultHttpOptions)
      }),
      network_id: '80001',
      confirmations: 2,
      timeoutBlocks: 200,
      skipDryRun: true,
      disableConfirmationListener: true
    },

    // Polygon Mainnet
    // https://github.com/ethereum-lists/chains/blob/master/_data/chains/eip155-137.json
    // https://polygonscan.com
    polygon: {
      provider: () => new Web3HttpProvider('https://polygon-rpc.com/', defaultHttpOptions),
      network_id: '137',
      confirmations: 2,
      timeoutBlocks: 200,
      skipDryRun: true,
      disableConfirmationListener: true
    }

  },

  // Set default mocha options here, use special reporters etc.
  // https://github.com/mochajs/mocha/blob/v8.1.2/lib/mocha.js#L97
  // https://mochajs.org/#command-line-usage
  // https://mochajs.org/api/mocha
  mocha: {
    color: true,
    //useColor: true,
    fullTrace: true,
    noHighlighting: false,
    //enableTimeouts: true,
    timeout: 180000,
    parallel: false
  },

  // Configure your compilers
  // https://www.trufflesuite.com/docs/truffle/reference/configuration#compiler-configuration
  // https://docs.soliditylang.org/en/v0.8.13/using-the-compiler.html
  compilers: {
    solc: {
      version: "pragma",  // https://github.com/trufflesuite/truffle/releases/tag/v5.2.0
      //parser: "solcjs",
      settings: {
        optimizer: {
          enabled: false,
          runs: 200
        }
        //evmVersion: "london"  // london, berlin, istanbul, petersburg, constantinople, byzantium
      }
    },
  },

  plugins: [
    //'truffle-contract-size'
  ]

};
