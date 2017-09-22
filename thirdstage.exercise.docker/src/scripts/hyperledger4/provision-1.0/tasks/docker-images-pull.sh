#! /bin/bash

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

host_arch=`jq -r '."host-arch"' ${config_file}`  # TODO What about '$HOSTTYPE'
if [ $? -ne 0 ]; then
  echo "Fail to parse host architecture from configuration file at ${config_file}."
  echo "Check and correct 'host-arch' in the configuration."
  exit 1
else
  echo "Host architecture : ${host_arch}"
fi

fabric_ver=`jq -r '."fabric-version"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse Fabric version from configuration file at ${config_file}."
  echo "Check and correct 'fabric-ver' in the configuration."
  exit 1
else
  echo "Fabric version : ${fabric_ver}"
fi

host_user=`jq -r '."host-account" | .username' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host user from configuration file at ${config_file}."
  echo "Check and correct 'host-account' in the configuration."
  exit 1
else
  echo "Host user : ${host_user}"
fi

# Pulls Fabric Orderer images for Orderer hosts.
orderer_host_ids=`jq -r '."fabric-orderers" | unique_by(."host-id") | .[] | ."host-id"' ${config_file}`
orderer_hosts=
for id in ${orderer_host_ids[@]}; do
  host=`jq -r --arg id ${id} '.hosts | .[] | select(.id == $id) | .address' ${config_file}`
  orderer_hosts=$orderer_hosts" $host"
done
if [[ -z ${orderer_hosts} ]]; then
  echo "Fabric Orderers are not defined or defined illegally at ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found Fabric Orderers : ${orderer_hosts}"
fi

for host in ${orderer_hosts[@]}; do echo ${host}; done | parallel --no-notice --bar --joblog /dev/stdout "
  ssh -o StrictHostKeyChecking=no ${host_user}@{} \"
  if [[ \\\$(docker images -q hyperledger/fabric-orderer:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      echo "Pulling Fabric Orderer."
      docker pull hyperledger/fabric-orderer:${host_arch}-${fabric_ver}
    fi
    if [[ \\\$(docker images -q hyperledger/fabric-tools:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-tools:${host_arch}-${fabric_ver}
    fi
  \"
"

# Pull Fabirc Peer and CouchDB images for Peer hosts.
peer_host_ids=`jq -r '."fabric-peers" | unique_by(."host-id") | .[] | ."host-id"' ${config_file}`
peer_hosts=
for id in ${peer_host_ids[@]}; do
  host=`jq -r --arg id ${id} '.hosts | .[] | select(.id == $id) | .address' ${config_file}`
  peer_hosts=$peer_hosts" $host"
done
if [[ -z ${peer_hosts} ]]; then
  echo "Fabric Peers are not defined or defined illegally at ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found Fabric Peers : ${peer_hosts}"
fi

for host in ${peer_hosts[@]}; do echo ${host}; done | parallel --no-notice --progress --bar --joblog /dev/stdout "
  ssh -o StrictHostKeyChecking=no ${host_user}@{} \"
    if [[ \\\$(docker images -q hyperledger/fabric-peer:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-peer:${host_arch}-${fabric_ver}
    fi
    if [[ \\\$(docker images -q hyperledger/fabric-couchdb:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-couchdb:${host_arch}-${fabric_ver}
    fi
    if [[ \\\$(docker images -q hyperledger/fabric-tools:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-tools:${host_arch}-${fabric_ver}
    fi
  \"
"

# Pull Kafka images for Kafka hosts.
kafka_host_ids=`jq -r '."kafkas" | unique_by(."host-id") | .[] | ."host-id"' ${config_file}`
kafka_hosts=
for id in ${kafka_host_ids[@]}; do
  host=`jq -r --arg id ${id} '.hosts | .[] | select(.id == $id) | .address' ${config_file}`
  kafka_hosts=$kafka_hosts" $host"
done
if [[ -z ${kafka_hosts} ]]; then
  echo "Kafka instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found Kafka hosts : ${kafka_hosts}"
fi

for host in ${kafka_hosts[@]}; do echo ${host}; done | parallel --no-notice --progress --bar --joblog /dev/stdout "
  ssh -o StrictHostKeyChecking=no ${host_user}@{} \"
    if [[ \\\$(docker images -q hyperledger/fabric-kafka:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-kafka:${host_arch}-${fabric_ver}
    fi
  \"
"

# Pull ZooKeeper images for ZooKeeper hosts.
zk_host_ids=`jq -r '."zookeepers" | unique_by(."host-id") | .[] | ."host-id"' ${config_file}`
zk_hosts=
for id in ${zk_host_ids[@]}; do
  host=`jq -r --arg id ${id} '.hosts | .[] | select(.id == $id) | .address' ${config_file}`
  zk_hosts=$zk_hosts" $host"
done
if [[ -z ${zk_hosts} ]]; then
  echo "ZooKeeper instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found ZooKeeper hosts : ${zk_hosts}"
fi

for host in ${zk_hosts[@]}; do echo ${host}; done | parallel --no-notice --progress --bar --joblog /dev/stdout "
  ssh -o StrictHostKeyChecking=no ${host_user}@{} \"
    if [[ \\\$(docker images -q hyperledger/fabric-zookeeper:${host_arch}-${fabric_ver} | wc -l) -eq 0 ]]; then
      docker pull hyperledger/fabric-zookeeper:${host_arch}-${fabric_ver}
    fi

  \"
"

# TODO Pull Fabric CA server images
ca_host_ids=`jq -r '."faric-ca-servers" | unique_by(."host-id") | .[] | ."host-id"' ${config_file}`
