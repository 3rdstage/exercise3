
# Standard Host Directory Layout

* `~/fabric/` : script files to run docker images or docker compose files
* `/etc/hyperledger/fabric/_nodename_/` : node configuration files such as `configtx.yaml`, `orderer.yaml` or `core.yaml`
* `/etc/hyperledger/msp/_nodename_` : certification files for nodes
* `/etc/hyperledger/tls/_nodename_` : TLS certification files for nodes


# Standard Container Directory Layout

* `/etc/hyperledger/fabric/` : node configuration files such as `configtx.yaml`, `orderer.yaml` or `core.yaml`
* `/etc/hyperledger/fabric-ca/server/` : Fabric CA server configuration files such as `fabric-ca-server-config.yaml`, equivalent to `FABRIC_CA_SERVER_HOME`
* `/etc/hyperledger/msp/orderer` : certification files for orderer
* `/etc/hyperledger/msp/peer` : certification files for peer
* `/etc/hyperledger/tls/orderer` :
* `/etc/hyperledger/tls/_peername_` : TLS certification files for peers