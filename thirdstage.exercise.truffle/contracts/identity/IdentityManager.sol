//pragma solidity 0.4.15;
pragma solidity ^0.5.0;

import "./Proxy.sol";


contract IdentityManager {
    uint adminTimeLock;
    uint userTimeLock;
    uint adminRate;

    event LogIdentityCreated(
        address indexed identity,
        address indexed creator,
        address owner,
        address indexed recoveryKey);

    event LogOwnerAdded(
        address indexed identity,
        address indexed owner,
        address instigator);

    event LogOwnerRemoved(
        address indexed identity,
        address indexed owner,
        address instigator);

    event LogRecoveryChanged(
        address indexed identity,
        address indexed recoveryKey,
        address instigator);

    event LogMigrationInitiated(
        address indexed identity,
        address indexed newIdManager,
        address instigator);

    event LogMigrationCanceled(
        address indexed identity,
        address indexed newIdManager,
        address instigator);

    event LogMigrationFinalized(
        address indexed identity,
        address indexed newIdManager,
        address instigator);

    mapping(address => mapping(address => uint)) owners;
    mapping(address => address) recoveryKeys;
    mapping(address => mapping(address => uint)) limiter;
    mapping(address => uint) public migrationInitiated;
    mapping(address => address) public migrationNewAddress;

    modifier onlyOwner(address identity) {
        require(isOwner(identity, msg.sender));
        _;
    }

    modifier onlyOlderOwner(address identity) {
        require(isOlderOwner(identity, msg.sender));
        _;
    }

    modifier onlyRecovery(address identity) {
        require(recoveryKeys[identity] == msg.sender);
        _;
    }

    modifier rateLimited(address identity) {
        require(limiter[identity][msg.sender] < (now - adminRate));
        limiter[identity][msg.sender] = now;
        _;
    }

    modifier validAddress(address addr) { //protects against some weird attacks
        require(addr != address(0));
        _;
    }

    /// @dev Contract constructor sets initial timelock limits
    /// @param _userTimeLock Time before new owner added by recovery can control proxy
    /// @param _adminTimeLock Time before new owner can add/remove owners
    /// @param _adminRate Time period used for rate limiting a given key for admin functionality
    constructor(uint _userTimeLock, uint _adminTimeLock, uint _adminRate) public {
        require(_adminTimeLock >= _userTimeLock);
        adminTimeLock = _adminTimeLock;
        userTimeLock = _userTimeLock;
        adminRate = _adminRate;
    }

    /// @dev Creates a new proxy contract for an owner and recovery
    /// @param owner Key who can use this contract to control proxy. Given full power
    /// @param recoveryKey Key of recovery network or address from seed to recovery proxy
    /// Gas cost of 289,311
    function createIdentity(address owner, address recoveryKey) public validAddress(recoveryKey) {
        Proxy identity = new Proxy();
        owners[address(identity)][owner] = now - adminTimeLock; // This is to ensure original owner has full power from day one
        recoveryKeys[address(identity)] = recoveryKey;
        emit LogIdentityCreated(address(identity), msg.sender, owner,  recoveryKey);
    }

    /// @dev Creates a new proxy contract for an owner and recovery and allows an initial forward call which would be to set the registry in our case
    /// @param owner Key who can use this contract to control proxy. Given full power
    /// @param recoveryKey Key of recovery network or address from seed to recovery proxy
    /// @param destination Address of contract to be called after proxy is created
    /// @param data of function to be called at the destination contract
    function createIdentityWithCall(address owner, address recoveryKey, address destination, bytes memory data) public validAddress(recoveryKey) {
        Proxy identity = new Proxy();
        owners[address(identity)][owner] = now - adminTimeLock; // This is to ensure original owner has full power from day one
        recoveryKeys[address(identity)] = recoveryKey;
        emit LogIdentityCreated(address(identity), msg.sender, owner,  recoveryKey);
        identity.forward(destination, 0, data);
    }

    /// @dev Allows a user to transfer control of existing proxy to this contract. Must come through proxy
    /// @param owner Key who can use this contract to control proxy. Given full power
    /// @param recoveryKey Key of recovery network or address from seed to recovery proxy
    /// Note: User must change owner of proxy to this contract after calling this
    function registerIdentity(address owner, address recoveryKey) public validAddress(recoveryKey) {
        require(recoveryKeys[msg.sender] == address(0)); // Deny any funny business
        owners[msg.sender][owner] = now - adminTimeLock; // This is to ensure original owner has full power from day one
        recoveryKeys[msg.sender] = recoveryKey;
        emit LogIdentityCreated(msg.sender, msg.sender, owner, recoveryKey);
    }

    /// @dev Allows a user to forward a call through their proxy.
    function forwardTo(Proxy identity, address destination, uint value, bytes memory data) public onlyOwner(address(identity)) {
        identity.forward(destination, value, data);
    }

    /// @dev Allows an olderOwner to add a new owner instantly
    function addOwner(Proxy identity, address newOwner) public onlyOlderOwner(address(identity)) rateLimited(address(identity)) {
        require(!isOwner(address(identity), newOwner));
        owners[address(identity)][newOwner] = now - userTimeLock;
        emit LogOwnerAdded(address(identity), newOwner, msg.sender);
    }

    /// @dev Allows a recoveryKey to add a new owner with userTimeLock waiting time
    function addOwnerFromRecovery(Proxy identity, address newOwner) public onlyRecovery(address(identity)) rateLimited(address(identity)) {
        require(!isOwner(address(identity), newOwner));
        owners[address(identity)][newOwner] = now;
        emit LogOwnerAdded(address(identity), newOwner, msg.sender);
    }

    /// @dev Allows an owner to remove another owner instantly
    function removeOwner(Proxy identity, address owner) public onlyOlderOwner(address(identity)) rateLimited(address(identity)) {
        // an owner should not be allowed to remove itself
        require(msg.sender != owner);
        delete owners[address(identity)][owner];
        emit LogOwnerRemoved(address(identity), owner, msg.sender);
    }

    /// @dev Allows an owner to change the recoveryKey instantly
    function changeRecovery(Proxy identity, address recoveryKey) public
        onlyOlderOwner(address(identity))
        rateLimited(address(identity))
        validAddress(recoveryKey)
    {
        recoveryKeys[address(identity)] = recoveryKey;
        emit LogRecoveryChanged(address(identity), recoveryKey, msg.sender);
    }

    /// @dev Allows an owner to begin process of transfering proxy to new IdentityManager
    function initiateMigration(Proxy identity, address newIdManager) public
        onlyOlderOwner(address(identity))
        validAddress(newIdManager)
    {
        migrationInitiated[address(identity)] = now;
        migrationNewAddress[address(identity)] = newIdManager;
        emit LogMigrationInitiated(address(identity), newIdManager, msg.sender);
    }

    /// @dev Allows an owner to cancel the process of transfering proxy to new IdentityManager
    function cancelMigration(Proxy identity) public onlyOwner(address(identity)) {
        address canceledManager = migrationNewAddress[address(identity)];
        delete migrationInitiated[address(identity)];
        delete migrationNewAddress[address(identity)];
        emit LogMigrationCanceled(address(identity), canceledManager, msg.sender);
    }

    /// @dev Allows an owner to finalize migration once adminTimeLock time has passed
    /// WARNING: before transfering to a new address, make sure this address is "ready to recieve" the proxy.
    /// Not doing so risks the proxy becoming stuck.
    function finalizeMigration(Proxy identity) public onlyOlderOwner(address(identity)) {
        require(migrationInitiated[address(identity)] != 0 && migrationInitiated[address(identity)] + adminTimeLock < now);
        address newIdManager = migrationNewAddress[address(identity)];
        delete migrationInitiated[address(identity)];
        delete migrationNewAddress[address(identity)];
        identity.transfer(newIdManager);
        delete recoveryKeys[address(identity)];
        // We can only delete the owner that we know of. All other owners
        // needs to be removed before a call to this method.
        delete owners[address(identity)][msg.sender];
        emit LogMigrationFinalized(address(identity), newIdManager, msg.sender);
    }

    function isOwner(address identity, address owner) public view returns (bool) {
        return (owners[identity][owner] > 0 && (owners[identity][owner] + userTimeLock) <= now);
    }

    function isOlderOwner(address identity, address owner) public view returns (bool) {
        return (owners[identity][owner] > 0 && (owners[identity][owner] + adminTimeLock) <= now);
    }

    function isRecovery(address identity, address recoveryKey) public view returns (bool) {
        return recoveryKeys[identity] == recoveryKey;
    }
}
