#!/bin/bash

# For more on CouchDB docker image for Hyperledger Fabric, refer followings
#  * https://hub.docker.com/r/hyperledger/fabric-couchdb/
#  * https://github.com/hyperledger/fabric/tree/v1.0.3/images/couchdb

# For web admin, access 'http://localhost:5884/_utils/'

readonly script_dir=$(cd `dirname $0` && pwd)
readonly image_name="hyperledger/fabric-couchdb"
readonly image_ver="x86_64-1.0.3"
readonly container_name=couch2
readonly port=6084

docker create \
--name ${container_name} \
-p ${port}:5984 \
-p 4469:4369 \
-p 9300:9100 \
--mount type=volume,src=${container_name},target=/opt/couchdb/data \
${image_name}:${image_ver}

docker cp ${script_dir}/couchdb.ini ${container_name}:/opt/couchdb/etc/local.d/docker.ini

docker start ${container_name}

if [ $? -ne 0 ]; then
  echo "Fail to run docker container"
  exit 1
fi

readonly wait_sec=5
echo "Waiting ${wait_sec} seconds for 'docker exec' to be completed."
sleep ${wait_sec}

# For more on single node setup, refer http://docs.couchdb.org/en/2.0.0/install/index.html#single-node-setup

curl -H 'Content-Type: application/json' \
--data '{"action":"enable_cluster","username":"admin","password":"admin!@34"}' \
"http://127.0.0.1:${port}/_cluster_setup"

curl -H 'Content-Type: application/json' \
--data '{"action":"finish_cluster"}' \
--user admin:admin!@34 \
"http://127.0.0.1:${port}/_cluster_setup"

result=$?

if [ $result -eq 0 ]; then
  echo ""
  echo "CouchDB container named '${container_name}' in single node has launched successfully."
  echo "Access 'http://localhost:${port}/_utils' to manage ${container_name}."
  echo "Data files : '/var/docker/${container_name}/opt/couchdb/data' in host filesystem."
  echo ""
else
  echo ""
  echo "Fail to setup single node for CouchDB container named '${container_name}'."
  echo ""
  exit $result
fi
