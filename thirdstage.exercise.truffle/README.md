### Initializing the source project

##### Install `go-ethereum` binaries and make them on `PATH`

*  https://geth.ethereum.org/downloads/


#### Install required pacakges running `npm install`

~~~~bash
mago.single$ npm install
~~~~

This may take a while. After that there will be local packages under `node_modules` directory.

#### Customize settings in `private-network-config.sh`

- `network_id` : Network ID of the Ethereum network
- `port` : the TCP listening port for the Ethereum node
- `rpc_port` : the RPC port for the Ethereum node
- `data_dir` : the data directory to store keys and ledger for the Ethereum node
- `passwd` : the shared(common) password for the accounts automatically generated
- `coin_base` : 

#### Initialize standalone private Ethereum network using `private-network-init.sh`

```bash
mago.single$ ./private-network-init.sh
```

#### Update `truffle.js` in accordance with `private-network-config.sh` and initialized Ethereum network

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


### Running the network and playing with stmart contracts

#### Start standalone private Ethereum network

~~~~bash
mago.single$ ./private-network-start.sh
~~~~

#### Compile smart contracts using `truffle`


#### Deploy smart contracts using `truffle`


### References

* [Truffle official documentation](http://truffleframework.com/docs/)



