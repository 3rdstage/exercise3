#!/bin/bash

# For more on CouchDB docker image for Hyperledger Fabric, refer followings
#  * https://hub.docker.com/r/hyperledger/fabric-couchdb/
#  * https://github.com/hyperledger/fabric/tree/v1.0.3/images/couchdb

# For web admin, access 'http://localhost:5984/_utils/'

readonly script_dir=$(cd `dirname $0` && pwd)
readonly image_name="hyperledger/fabric-couchdb"
readonly image_ver="x86_64-1.0.3"
readonly container_name=${1:-couch1}
readonly port=${2:-5984}

docker create \
--name ${container_name} \
-p ${port}:5984 \
-p 4369:4369 \
-p 9110:9100 \
-v /var/docker/${container_name}/data:/opt/couchdb/data \
${image_name}:${image_ver}

docker cp ${script_dir}/couchdb.ini couch1:/opt/couchdb/etc/local.d/docker.ini

docker start couch1

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

# Downloading sample data for performance test from web site
# The file contains more than 76,000 lines
# For more, refer https://docs.influxdata.com/influxdb/v1.2/query_language/data_download/

if [ ! -f ${script_dir}/${container_name}-sample-data.txt ]; then
   echo "Downloading sample data from remote web server. This may take a few minutes."
   curl https://s3.amazonaws.com/noaa.water-database/NOAA_data.txt -o ${script_dir}/${container_name}-sample-data.txt
   sed -i '1,8 d' ${script_dir}/${container_name}-sample-data.txt
fi


