#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"

# echo $base_dir

if [ -f "${base_dir}/data/geth/chaindata/CURRENT" ]; then
  echo "The standalone Quorum network seems to be initialized already."
  echo "Check the contents directory of '${base_dir}'."
  echo "If you want init again, backup '${base_dir}' directory, delete the directory and then try this script."
  exit 101
fi

mkdir -p "${base_dir}/data/geth"
mkdir -p "${base_dir}/data/keystore"
mkdir -p "${base_dir}/constellation"
mkdir -p "${base_dir}/logs"

# Move to base directory
cd "${base_dir}"

# Generate Quorum nodekey
bootnode -genkey "data/geth/nodekey"

# Get Quorum node ID
bootnode -nodekey "data/geth/nodekey" -writeaddress > "data/geth/nodeid"

# Get primary IP address of local machine
readonly addr=`hostname -I | awk '{print $1}'`

# Create permissioned-nodes.json file and static-nodes.json
readonly nodeid=`cat data/geth/nodeid`
cat << HERE > "data/permissioned-nodes.json"
[
  "enode://${nodeid}@${addr}:${quorum[port]}?discport=${quorum[discport]}&raftport=${quorum[raftport]}"
]
HERE
cp "data/permissioned-nodes.json" "data/static-nodes.json"

# Create account password file
echo ${accounts[password]} > "data/passwd"

# Generate Quorum accounts
# TODO Check warning that says 'WARN [03-07|19:24:46] No etherbase set and no accounts found as default'
declare -a addrs
declare allocs="{"
for (( i=0; i < ${accounts[count]}; i++ )); do
  addrs[i]=`geth account new --keystore 'data/keystore' --password 'data/passwd'`
  addrs[i]=${addrs[i]:10:40}
  allocs="${allocs}\n    \"0x${addrs[i]}\" : { \"balance\" : \"${balances[i]}\" },"
done

allocs="${allocs%,}\n  }"
# echo -e ${allocs}

# Generate genesis file
sed "s/\"@allocs@\"/${allocs}/g" "${script_dir}/genesis.template.json" | \
  sed "s/@coinbase@/0x${addrs[0]}/g" > "genesis.json"

# Delete account password file
# rm -f "data/passwd"

# Create Constellation configuration file
# Note that '-i' option of sed is not used to support Mac OS
sed "s/@port@/${constellation[port]}/g" "${script_dir}/tm.template.conf" | \
  sed "s/@verbosity@/${constellation[verbosity]}/g" > "constellation/tm.conf"

# Generate Constellation's key files
cd constellation
constellation-node --generatekeys=tm
cd ..

# Create empty log files
if [ ! -f "logs/quorum.log" ]; then
  echo '' > "logs/quorum.log"
fi

if [ ! -f "logs/constellation.log" ]; then
  echo '' > "logs/constellation.log"
fi

# Init Quorum node using genesis.json
geth --datadir "data" init genesis.json


