# Provisioning Procedure

* Perpare headerquarter host
* Perpare hosts
* Write down provisioning script
* Pull Docker images for Fabric Orderer, Fabric Peer, Fabric Tools, Kafka, ZooKeeper and so on.
* Generate configtx.yaml file
* Generate genesis blocks and channel configuration transaction files
* Generate crypto-config.yaml file
* Generate crypto files including keys and certificates.

* Setup Docker Swarm and Docker network
* Generate ZooKeeper launch scripts and launch ZooKeeper containers
* Generate Kafka launch scripts and launch Kafka containers


# Standard Host Directory Layout

* `~/fabric/_version_/setup`
    * scripts and configurations to provision fabric of specific version
    * `scripts/`, `config/`, `crypto/`.

* `~/fabirc/clients`
    * `~/fabric/clients/bankcert`

* `~/docker/etc/hyperledger/fabric/_nodename_/`
    * node configuration files such as `configtx.yaml`, `orderer.yaml` or `core.yaml`

* `~/docker/etc/hyperledger/msp/_nodename_`
    * certification files for nodes

* `~/docker/etc/hyperledger/tls/_nodename_`
    * TLS certification files for nodes

* `/var/docker/hyperledger/peers/_peer-container-name_`
    * data files of Fabric Peer node
    * owner:root, group:root
    * `chaincodes/`, `ledgersData/`, `peer.pid`

* `/var/docker/hyperledger/couchdbs/_couchdb-container-name_`
    * data files for CouchDB node
    * owner:root, group:root
    * `data/`, `var/log/`

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



# Resources on Bash Programming

* [Bash Reference Manual](https://www.gnu.org/software/bash/manual/bash.html)
* [Introduction to if](http://tldp.org/LDP/Bash-Beginners-Guide/html/sect_07_01.html)
* [if statements](https://en.wikibooks.org/wiki/Bash_Shell_Scripting#if_statements)
* [The for loop](http://tldp.org/LDP/Bash-Beginners-Guide/html/sect_09_01.html)
* [Special Parameters](https://www.gnu.org/software/bash/manual/bashref.html#Special-Parameters)
    * `$*, $@, $#, $?, $-, $$, $0, $!, $_`
* [Shell Parameter Expansion](http://www.gnu.org/software/bash/manual/bash.html#Shell-Parameter-Expansion)
    * `${parameter:-word}, ${parameter:=word}, ${parameter:?word}, ${parameter:+word}, ${parameter##word}, ${parameter%%word}, ...`

* [Bash Guide for Beginners](http://tldp.org/LDP/Bash-Beginners-Guide/html/index.html)
* [Bash Shell Scripting](https://en.wikibooks.org/wiki/Bash_Shell_Scripting)
