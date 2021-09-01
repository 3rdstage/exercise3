
// https://github.com/lorenwest/node-config/wiki/Special-features-for-JavaScript-configuration-files  
  
module.exports = { 
  app: {
    port: 3000
  },
  
  web3: {
    url: `wss://kovan.infura.io/ws/v3/${process.env.INFURA_PROJECT_ID}`,
    from: '0xb009cd53957c0D991CAbE184e884258a1D7b77D9'
  },
  
  smartContracts: {
    web3TestContract: {
      addresses: {
        kovan: '0x78E9099A7ab23a7a5616Df1da5AB151E22f398b1'
      }
    },
    solidityTestContract: {
      addresses: {
        kovan: '0x1b25c314a3c36AeA84F586FB1362e30eD63007e9'
      }
    }    
  },
  
  testAccounts: [{ 
      address : '0xb009cd53957c0D991CAbE184e884258a1D7b77D9',
      key : '0x052fdb8f5af8f2e4ef5c935bcacf1338ad0d8abe30f45f0137943ac72f1bba1e'
    }, {
      address : '0x05f9301Be8F3C133fC474F8d538fD732CaCa274c',
      key : '0x6006fc64218112913e638a2aec5bd25199178cfaf9335a83b75c0e264e7d9cee'
    }, {
      address : '0x3DC9b4063a130535913137E40Bed546Ff93b1131',
      key : '0x724443258d598ee09e79bdbdc4af0792a69bd80082f68180157208aa6c5437de'
    }, {
      address : '0x770b1A8d293d152B8Cc9fC01531B1baB3469AF05',
      key : '0x00f84e1eaf2918511f4690fb396c89928bebfbe5d96cd821069ecf16e921a4ee'
    }, {
      address : '0xAB3ca295454D4A4de79aE32474d2C82f2D0836b1',
      key : '0x78394a06447e6688317ee920cefd3b992dee3d9ee9cb2462f22ab730723fab4a'
    }
  ]
}