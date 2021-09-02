
module.exports = (web3, db) => {
  return [ require('./basic'), require('./test-contract')(web3, db) ];
}

