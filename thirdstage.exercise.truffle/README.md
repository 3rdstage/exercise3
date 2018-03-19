### Initializing the source project

##### Install `go-ethereum` binaries and make them on `PATH`

*  https://geth.ethereum.org/downloads/


#### Install required pacakges running `npm install`

~~~~bash
$ npm install
~~~~

This may take a while. After that there will be local packages under `node_modules` directory.

#### Customize settings in `config.sh`

- `quorum[networkid]` : Network ID of the Quorum network
- `quorum[name]` : the name of solo Quorum node, any alpha-numeric string 
- `quorum[port]` : the TCP listening port of Quorum node, default : 30303
- `quorum[type]` : 'permissioned', do NOT change
- `quorum[rpcaddr]` : the binding address for the JSON-RPC service by the Quorum node
- `quorum[rpcaddr]` :

#### Initialize standalone private Ethereum network using `init.sh`

```bash
$ ./scripts/quorum/solo/init.sh
```

#### Update `truffle.js` in accordance with `config.sh` and initialized Ethereum network

Update `port`, `network_id`, and `from` attributes of `networks.development` node

```json
module.exports = {
    networks: {
      development: {
        host: "127.0.0.1",
        port: 8545,
        network_id: 31,
        from: "47173ba217c39dea60c7d115ea956a3e293fb012",
        gas: 0x2fefd8
      }
    }
};
```


### Running the Quorum network

#### Start standalone private Ethereum network

~~~~bash
$ ./scripts/quorum/solo/start.sh
~~~~

#### Run `tcpdump` to inspect JSON-RPC conversations

#### Compile smart contracts using `truffle`


#### Deploy smart contracts using `truffle`


### References

* [Truffle official documentation](http://truffleframework.com/docs/)



