#! /bin/bash

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

# @TODO Warn that the ZooKeeper launcher scripts under 'generated/scripts' directory would be overwritten or removed.
# @TODO Remove or move the old launcher scripts.

readonly host_arch=`jq -r '."host-arch"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host architecture from configuration file at ${config_file}."
  echo "Check and correct 'host-arch' in the configuration."
  exit 1
else
  echo "Host architecture : ${host_arch}"
fi

readonly fabric_ver=`jq -r '."fabric-version"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse Fabric version from configuration file at ${config_file}."
  echo "Check and correct 'fabric-ver' in the configuration."
  exit 1
else
  echo "Fabric version : ${fabric_ver}"
fi

readonly host_user=`jq -r '."host-account".username' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host user from configuration file at ${config_file}."
  echo "Check and correct 'host-account' in the configuration."
  exit 1
else
  echo "Host user : ${host_user}"
fi

readonly zks=`jq -rc '."zookeepers" | .[]' ${config_file}`
if [[ -z ${zks} ]]; then
  echo "ZooKeeper instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found ZooKeeper configurations : ${zks}"
  echo "Generating scripts to launch ZooKeeper instances."
fi
readonly zk_ids=`echo $zks | jq -rc '.id'`
readonly zk_tick_time_0=`jq -r '."zookeeper-config-default"."tick-time"' ${config_file}` # mandatory property
readonly zk_init_limit_0=`jq -r '."zookeeper-config-default"."init-limit"' ${config_file}` # mandatory property
readonly zk_sync_limit_0=`jq -r '."zookeeper-config-default"."sync-limit"' ${config_file}` # mandatory property
readonly zk_client_port_0=`jq -r '."zookeeper-config-default"."client-port"' ${config_file}` # mandatory property
readonly zk_quorum_port_0=`jq -r '."zookeeper-config-default"."quorum-port"' ${config_file}` # mandatory property
readonly zk_election_port_0=`jq -r '."zookeeper-config-default"."election-port"' ${config_file}` # mandatory property
for zk in ${zks[@]}; do
  zk_id=`echo $zk | jq -r '.id'`
  zk_name=`echo $zk | jq -r '.name'`

  zk_servers=
  quorum_port=
  election_port=
  host_id=
  host_addr=
  host_quorum_port=
  host_election_port=
  for id in ${zk_ids[@]}; do
    if [[ ${id} == ${zk_id} ]]; then
      quorum_port=`echo ${zk} | jq -r '.config."quorum-port"'`
      if [ "${quorum_port}" == "null" ]; then quorum_port=${zk_quorum_port_0}; fi
      election_port=`echo ${zk} | jq -r '.config."election-port"'`
      if [ "${election_port}" == "null" ]; then election_port=${zk_election_port_0}; fi
      zk_servers=${zk_servers}"server.${id}=0.0.0.0:${quorum_port}:${election_port} "
    else
      zk2=`jq -rc --arg id ${id} '.zookeepers | .[] | select(.id == $id)' ${config_file}`
      host_id=`echo ${zk2} | jq -r '."host-id"'`
      host_addr=`jq -rc --arg id ${host_id} '.hosts | .[] | select(.id == $id).address' ${config_file}`
      host_quorum_port=`echo ${zk2} | jq -r '.docker."host-ports"."quorum-port"'`
      host_election_port=`echo ${zk2} | jq -r '.docker."host-ports"."election-port"'`
      zk_servers=${zk_servers}"server.${id}=${host_addr}:${host_quorum_port}:${host_election_port} "
    fi
  done

  tick_time=`echo $zk | jq -r '."config"."tick-time"'`
  if [ "${tick_time}" == "null" ]; then tick_time=${zk_tick_time_0}; fi
  init_limit=`echo $zk | jq -r '."config"."init-limit"'`
  if [ "${init_limit}" == "null" ]; then init_limit=${zk_init_limit_0}; fi
  sync_limit=`echo $zk | jq -r '."config"."sync-limit"'`
  if [ "${sync_limit}" == "null" ]; then sync_limit=${zk_sync_limit_0}; fi
  host_id=`echo ${zk} | jq -r '."host-id"'`
  host_addr=`jq -rc --arg id ${host_id} '.hosts | .[] | select(.id == $id).address' ${config_file}`
  host_quorum_port=`echo ${zk} | jq -r '.docker."host-ports"."quorum-port"'`
  host_election_port=`echo ${zk} | jq -r '.docker."host-ports"."election-port"'`
  cat <<HERE > ${base_dir}/generated/bin/zk-run-${zk_name}.sh
    docker run -d \\
    --name ${zk_name} \\
    -p ${host_addr}:${host_quorum_port}:${quorum_port} \\
    -p ${host_addr}:${host_election_port}:${election_port} \\
    -e ZOO_MY_ID=${zk_id} \\
    -e ZOO_SERVERS=${zk_servers} \\
    -e ZOO_TICK_TIME ${tick_time} \\
    -e ZOO_INIT_LIMIT ${init_limit} \\
    -e ZOO_SYNC_LIMIT ${sync_limit} \\
    hyperledger/fabric-zookeeper:${host_arch}-${fabric_ver}
HERE

  if [ -f ${base_dir}/generated/bin/zk-run-${zk_name}.sh ]; then
    echo "Succefully generated ZooKeeper launch script for '${zk_name}'."
    cat ${base_dir}/generated/bin/zk-run-${zk_name}.sh
  else
    echo "Fail to generate ZooKeeper launch script for '${zk_name}' into 'generated/bin/zk-run-${zk_name}.sh'."
  fi
done

