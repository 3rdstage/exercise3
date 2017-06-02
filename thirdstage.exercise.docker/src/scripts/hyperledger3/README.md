
# Standard Host Directory Layout

* `~/fabric/scripts/_version_`
    * script files to run docker images or docker compose files

* `~/docker/etc/hyperledger/fabric/_nodename_/`
    * node configuration files such as `configtx.yaml`, `orderer.yaml` or `core.yaml`

* `~/docker/etc/hyperledger/msp/_nodename_`
    * certification files for nodes

* `~/docker/etc/hyperledger/tls/_nodename_`
    * TLS certification files for nodes

* `~/docker/opt/couchdb/_containername_`
    * data, log files for CouchDB
    * `~/docker/opt/couchdb/_containername_/data`
    * `~/docker/opt/couchdb/_containername_/var/log`


# Standard Container Directory Layout

* `/etc/hyperledger/fabric/`
    * node configuration files such as `configtx.yaml`, `orderer.yaml` or `core.yaml`

* `/etc/hyperledger/fabric-ca/server/`
    * Fabric CA server configuration files such as `fabric-ca-server-config.yaml`, equivalent to `FABRIC_CA_SERVER_HOME`

* `/etc/hyperledger/msp/orderer`
    * certification files for orderer

* `/etc/hyperledger/msp/peer`
    * certification files for peer

* `/etc/hyperledger/tls/orderer`

* `/etc/hyperledger/tls/_peername_`
    * TLS certification files for peers

* `/opt/couchdb/`
    * `/opt/couchdb/etc`
    * `/opt/couchdb/data`
    * `/opt/couchdb/var/log`


# Environment Varialbes

* `GOPATH`

* `FABRIC_VERSION` -

* `COUCHDB_LOG_LEVEL` - default : info
* `COUCHDB_PORT` - default : 5984


