
// https://github.com/lorenwest/node-config/wiki/Special-features-for-JavaScript-configuration-files  
  
module.exports = { 
  app: {
    port: 3000
  },
  
  web3: {
    url: `https://kovan.infura.io/v3/${process.env.INFURA_PROJECT_ID}`
  },
  
  smartContracts: {
    web3TestContract: {
      addresses: {
        kovan: '0x821BAf1411Bdc650d7DA59619eE7ac191dd0C19E'
      }
    },
    solidityTestContract: {
      addresses: {
        kovan: '0xAcD4458768Ee476b3Fffaa59D3e5d885b78EA4Da'
      }
    }    
  }
}