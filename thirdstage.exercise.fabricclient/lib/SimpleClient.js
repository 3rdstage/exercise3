/**
 * http://usejsdoc.org/
 */



'use strict';

/**
*
* @class
*/
var SimpleClient = class {

  var client;
  var chain;


  constructor(){

  }


  /**
   * @param {Object} target
   * @param {string} chaincodeId the ID of chaincode to call
   * @param {Object[]} an array of arguments specific to the chaincode 'innvoke' that represent a query invocation on that chaincode
   * @param {Object} [transientMap] map that can be used by the chaincode but not saved in the ledger, such as cryptographic information for encryption
   *
   * @returns {Promise} A Promise for an array of byte array results from the chaincode on all Endorsing Peers
   *
   * @see {@link https://fabric-sdk-node.github.io/Chain.html#queryByChaincode|Chain.queryByChaincode(request)}
   */
  queryByChaincode(target, chaincodeId, args, transientMap){


  }


}
