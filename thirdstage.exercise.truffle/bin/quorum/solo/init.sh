#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"

# echo $base_dir

if [ -f "${base_dir}/data/geth/chaindata/CURRENT" ]; then
  echo "The solo Quorum network seems to be initialized already."
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

# Copy genesis file
cp "${script_dir}/genesis.json" "genesis.json"


# Generate Quorum nodekey
bootnode -genkey "data/geth/nodekey"

# Get Quorum node ID
bootnode -nodekey "data/geth/nodekey" -writeaddress > "data/geth/nodeid"

# Create permissioned-node.json file
cat << HERE > "data/permissioned-nodes.json"
  {

  }
HERE

# Create account password file
echo ${accounts[password]} > "data/passwd"

# Generate Quorum accounts
# TODO Check warning that says 'WARN [03-07|19:24:46] No etherbase set and no accounts found as default'
for (( i=0; i < ${accounts[count]}; i++ )); do
  geth account new --keystore 'data/keystore' --password 'data/passwd'
done

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


