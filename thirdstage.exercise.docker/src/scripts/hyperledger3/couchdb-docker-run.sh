#! /bin/bash

# For more on CouchDB configuration, refer
#   -http://docs.couchdb.org/en/2.0.0/config/
#   -http://docs.couchdb.org/en/2.0.0/config-ref.html : CouchDB configuration reference

name=couchdb0
port=${COUCHDB_PORT:-5984}

sudo rm -rf ~/docker/opt/couchdb/$name

docker run -itd \
--name $name \
-p $port:5984 \
-v ~/docker/opt/couchdb/$name/data:/opt/couchdb/data \
-v ~/docker/opt/couchdb/$name/var/log:/opt/couchdb/var/log \
hyperledger/fabric-couchdb /bin/bash

echo "Waiting 2 seconds for CouchDB to be launched completely."
sleep 2

docker exec -it $name bash -c "cat >> /opt/couchdb/etc/local.d/docker.ini << HERE
[log]
file = /opt/couchdb/var/log/couch.log
writer = file
level = ${COUCHDB_LOG_LEVEL:-info}
HERE"

echo "Waiting 2 seconds for 'docker exec' to be completed."
sleep 2

docker exec -itd $name "/opt/couchdb/bin/couchdb"

if [ $? -eq 0 ]; then
  echo ""
  echo "CouchDB container named '$name' has launched successfully."
  echo "Access 'http://localhost:$port/_utils' to manage $name."
  echo "Data files : '~/docker/opt/couchdb/$name/data' in host filesystem."
  echo "Log files  : '~/docker/opt/couchdb/$name/var/log' in host filesystem."
  echo ""
fi

