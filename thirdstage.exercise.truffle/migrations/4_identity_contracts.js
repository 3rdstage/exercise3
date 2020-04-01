// https://github.com/uport-project/uport-identity/blob/v2.0.1/migrations/2_deploy_identity_managers.js

const IdentityManager = artifacts.require('./identity/IdentityManager.sol')
const TxRelay = artifacts.require('./identity/TxRelay.sol')
const MetaIdentityManager = artifacts.require('./identity/MetaIdentityManager.sol')

const USER_TIME_LOCK = 3600
const ADMIN_TIME_LOCK = 129600
const ADMIN_RATE = 1200

module.exports = function (deployer) {
  deployer.deploy(IdentityManager, USER_TIME_LOCK, ADMIN_TIME_LOCK, ADMIN_RATE).then(() => {
    return deployer.deploy(TxRelay)
  }).then(() => {
    return deployer.deploy(MetaIdentityManager, USER_TIME_LOCK, ADMIN_TIME_LOCK, ADMIN_RATE, TxRelay.address)
  })
}