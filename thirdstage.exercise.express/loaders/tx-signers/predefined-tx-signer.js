const AbstractTxSigner = require('./abstract-tx-signer');

'use strict';

module.exports = class PredefinedTxSigner extends AbstractTxSigner{
  constructor() {
    super();
    console.log('PredefinedTxSigner instance created');
  }
  
  sign(tx, signer){
    console.log(`PredefinedTxSigner.sign - ${tx}, ${signer}`);
  }
}
