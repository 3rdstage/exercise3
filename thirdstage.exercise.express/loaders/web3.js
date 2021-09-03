const config = require('config');
const Web3 = require('web3');

const web3WsProvider = new Web3.providers.WebsocketProvider(config.web3.url, {
  // https://github.com/ChainSafe/web3.js/tree/1.x/packages/web3-providers-ws
  clientConfig: {
    maxReceivedFrameSize: 100000000,   // bytes - default: 1MiB
    maxReceivedMessageSize: 100000000, // bytes - default: 8MiB
    keepalive: true,
    keepaliveInterval: 60000 // ms
  }
});
const web3HttpProvider = new Web3.providers.HttpProvider(config.web3.url, {
  // https://web3js.readthedocs.io/en/v1.3.6/web3.html#configuration
  // https://github.com/ChainSafe/web3.js/blob/1.x/packages/web3-providers-http/src/index.js
  keepAlive: true, 
  withCredentials: false, 
  timeout: 20000
});

const web3 = new Web3(web3HttpProvider);
web3.eth.Contract.defaultAccount = config.get('web3.from');


module.exports = web3;