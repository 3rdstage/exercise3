module.exports = function(callback) {

  //References
  //  - http://bit.ly/2yI2GL3
  //  - https://github.com/ethereumjs/ethereumjs-tx/blob/master/docs/classes/transaction.md

  const ethTx = require('ethereumjs-tx');

  const txData = {
    nonce: '0x0',
    gasPrice: '0x09184e72a000',
    gasLimit: '0x30000',
    to: '0x99322780C19B664e9902Ff1031549da575De8F3B',
    value: '0x00',
    data: '',
    v: '0x1c', // ethereum mainnet
    r: 0,
    s: 0
  };

  tx = new ethTx(txData);
  console.log('RLP-Encoded Tx: 0x' + tx.serialize().toString('hex'));

  console.log('Tx Hash: 0x' + tx.hash().toString('hex'));

  const privKey = Buffer.from('91c8360c4cb4b5fac45513a7213f31d4e4a7bfcb4630e9fbf074f42a203ac0b9', 'hex');
  tx.sign(privKey);

  console.log(tx.toJSON());

  console.log('Signed Raw Tx: 0x' + tx.serialize().toString('hex'));

}