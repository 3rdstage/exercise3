const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
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
require('request').debug = true

const web3 = new Web3(web3HttpProvider);
web3.eth.Contract.defaultAccount = config.get('web3.from');


const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const restRouters = require('./routes/rest');
const ethRouters = require('./routes/eth')(web3, null);

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true })); 
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/rest', restRouters);
app.use('/eth', ethRouters);

console.log("Loaded application");
console.log("Cofiguration : ");
console.log(config.util.toObject());

module.exports = app;
