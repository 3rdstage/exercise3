
// https://github.com/lorenwest/node-config/wiki/Special-features-for-JavaScript-configuration-files  
  
module.exports = { 
  app: {
    port: 3000
  },
  
  web3: {
    url: `https://rinkeby.infura.io/v3/${process.env.INFURA_PROJECT_ID}`
  }
}