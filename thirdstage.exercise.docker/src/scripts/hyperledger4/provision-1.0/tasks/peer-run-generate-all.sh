#! /bin/bash

# References
#   - Typical peer configuration : https://github.com/hyperledger/fabric/blob/v1.0.2/sampleconfig/core.yaml
#   - Peer configuration from Fabric official test : https://github.com/hyperledger/fabric/blob/v1.0.2/test/feature/docker-compose/docker-compose-kafka.yml


readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

# @TODO Warn that the Peer launcher scripts under 'generated/bin' directory would be overwritten or removed.
# @TODO Rename or move the old launcher scripts.

# Remove previous scripts
rm -f ${base_dir}/generated/bin/peer-run*
if [ $? -ne 0 ]; then
  echo "Fail to remove previously generated Peer launch scripts."
  exit 1
fi

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

readonly peers=`jq -rc '."peers" | .[]' ${config_file}`
if [[ -z ${peers} ]]; then
  echo "Peer instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found Peer configurations : ${peers}"
  echo "Generating scripts to launch Peer instances."
fi
# readonly names=`echo ${peers} | jq -rc '.name'`

# Parse default Peer configuration
readonly port_0=`jq -r '."peer-config-default".port' ${config_file}`
readonly cc_port_0=`jq -r '."peer-config-default"."chaincode-port"' ${config_file}`
readonly ev_port_0=`jq -r '."peer-config-default"."event-port"' ${config_file}`
readonly loglvl_default_0=`jq -r '."peer-config-default"."logging-levels".default' ${config_file}`
readonly loglvl_cauthdsl_0=`jq -r '."peer-config-default"."logging-levels".cauthdsl' ${config_file}`
readonly loglvl_gossip_0=`jq -r '."peer-config-default"."logging-levels".gossip' ${config_file}`
readonly loglvl_ledger_0=`jq -r '."peer-config-default"."logging-levels".ledger' ${config_file}`
readonly loglvl_msp_0=`jq -r '."peer-config-default"."logging-levels".msp' ${config_file}`
readonly loglvl_policies_0=`jq -r '."peer-config-default"."logging-levels".policies' ${config_file}`
readonly loglvl_grpc_0=`jq -r '."peer-config-default"."logging-levels".grpc' ${config_file}`
readonly loglvl_committer_0=`jq -r '."peer-config-default"."logging-levels".committer' ${config_file}`
readonly gossip_org_leader_0=`jq -r '."peer-config-default".gossip."org-leader"' ${config_file}`

# Generate launch script for each Peer instance
for peer in ${peers[@]}; do

  name=`echo ${peer} | jq -r '.name'`
  port=`echo ${peer} | jq -r '.port'`
  cc_port=`echo ${peer} | jq -r '."chaincode-port"'`
  ev_port=`echo ${peer} | jq -r '."event-port"'`



  host_id=`echo ${peer} | jq -r '."host-id"'`
  host_addr=`jq -rc --arg id ${host_id} '.hosts | .[] | select(.id == $id).address' ${config_file}`

  cat <<HERE > ${base_dir}/generated/bin/peer-run-${name}-${host_id}.sh
    docker run -d \\
    --name ${name} \\
    -e CORE_LOGGING_LEVEL=info \\
    -e CORE_LOGGING_CAUTHDSL=warning \\
    -e CORE_LOGGING_GOSSIP=warning \\
    -e CORE_LOGGING_LEDGER=info \\
    -e CORE_LOGGING_MSP=warning \\
    -e CORE_LOGGING_POLICIES=warning \\
    -e CORE_LOGGING_GRPC=error \\
    -e CORE_LOGGING_COMMITTER=debug \\
    -e CORE_NEXT=true \\
    -e CORE_PEER_ID=${name} \\
    -e CORE_PEER_NETWORKID=${name} \\
    -e CORE_PEER_ADDRESS=${host_addr}:${port} \\
    -e CORE_PEER_CHAINCODELISTENADDRESS=${host_addr}:${chaincode_port} \\
    -e CORE_PEER_ENDORSER_ENABLED=true \\
    -e CORE_PEER_LOCALMSPID=${org_name} \\
    -e CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/ \\
    -e CORE_PEER_GOSSIP_USELEADERELECTION=false \\
    -e CORE_PEER_GOSSIP_ORGLEADER=${gossip_org_leader} \\
    -e CORE_PEER_GOSSIP_EXTERNALENDPOINT=${gossip_ext_endpt} \\
    -e CORE_PEER_GOSSIP_BOOTSTRAP=${gossip_bootstrap}:7051 \\
    -e CORE_PEER_GOSSIP_SKIPHANDSHAKE=true \\
    -e CORE_PEER_GOSSIP_PROPAGATEPEERNUM=8 \\
    -e CORE_PEER_GOSSIP_PULLINTERVAL=1s \\
    -e CORE_PEER_GOSSIP_REQUESTSTATEINFOINTERVAL=2s \\
    -e CORE_PEER_GOSSIP_PUBLISHSTATEINFOINTERVAL=2s \\
    -e CORE_PEER_GOSSIP_PUBLISHCERTPERIOD=180s \\
    -e CORE_PEER_GOSSIP_RECVBUFFSIZE=1000 \\
    -e CORE_PEER_GOSSIP_SENDBUFFSIZE=1000 \\
    -e CORE_PEER_EVENTS_BUFFERSIZE=100 \\
    -e CORE_PEER_EVENTS_TIMEOUT=10ms \\
    -e CORE_PEER_TLS_ENABLED=true \\
    -e CORE_PEER_TLS_CERT_FILE=/etc/hyperledger/tls/server.crt \\
    -e CORE_PEER_TLS_KEY_FILE=/etc/hyperledger/tls/server.key \\
    -e CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/tls/ca.crt \\
    -e CORE_PEER_PROFILE_ENABLED=false \\
    -e CORE_CHAINCODE_KEEPALIVE=${cc_keepalive} \\
    -e CORE_CHAINCODE_LOGGING_LEVEL=${cc_loglvl} \\
    -e CORE_VM_ENDPOINT=unix:///host/var/run/docker.sock \\
    -e CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=host \\
    -e CORE_LEDGER_STATE_STATEDATABASE=CouchDB \\
    -e CORE_LEDGER_STATE_COUCHDBCONFIG_COUCHDBADDRESS=${couch_address}:${couch_port} \\
    -e CORE_LEDGER_STATE_COUCHDBCONFIG_USERNAME=${couch_username} \\
    -e CORE_LEDGER_STATE_COUCHDBCONFIG_PASSWORD=${couch_passwd} \\
    -e CORE_LEDGER_STATE_HISTORYDATABASE=true \\
    -v /var/run/:/host/var/run/ \\
    -v ~/provision/production/${name}:/var/hyperledger/production/ \\
    -v ~/provision/generated/crypto:/var/hyperledger/configs/${name} \\
    -v ~/provision/generated/crypto/peerOrganizations/${org_name}/peers/${name}/msp:/etc/hyperledger/msp \\
    -v ~/provision/generated/crypto/peerOrganizations/${org_name}/peers/${name}/tls:/etc/hyperledger/tls \\
    --workdir /opt/gopath/src/github.com/hyperledger/fabric \\
    hyperledger/fabric-peer:${host_arch}-${fabric_ver}
HERE

if [ -f ${base_dir}/generated/bin/peer-run-${name}-${host_id}.sh ]; then
    echo "Succefully generated Peer launch script for '${name}'."
    cat ${base_dir}/generated/bin/peer-run-${name}-${host_id}.sh
  else
    echo "Fail to generate Peer launch script for '${name}' into 'generated/bin/peer-run-${name}-${host_id}'."
    exit 1
  fi
done