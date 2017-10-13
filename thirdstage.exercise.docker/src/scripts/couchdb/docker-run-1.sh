#!/bin/bash

# For more on CouchDB docker image for Hyperledger Fabric, refer followings
#  * https://hub.docker.com/r/hyperledger/fabric-couchdb/
#  * https://github.com/hyperledger/fabric/tree/v1.0.3/images/couchdb

# For web admin, access 'http://localhost:5984/_utils/'

readonly image_name="hyperledger/fabric-couchdb"
readonly image_ver="x86_64-1.0.3"
readonly container_name=${1:-couch1}

docker run -d \
--name ${container_name} \
-p 5984:5984 \
-p 4369:4369 \
-p 9110:9100 \
-v /var/docker/couchdb/${container_name}/data:/opt/couchdb/data \
${image_name}:${image_ver}
