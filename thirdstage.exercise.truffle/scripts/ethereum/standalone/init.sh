#!/bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"
# echo "Configuration for private Ethereum network - networkid: ${network_id}, port: ${port}, rpcport: ${rpc_port}, datadir: ${data_dir}, coinbase: ${coin_base}"

if [ -f "${data_dir}/geth/chaindata/CURRENT" ]; then
  echo "The private network seems to be initiated already. Check the directory of '${data_dir}'"
  exit 101
fi

if [ -f "${data_dir}/genesis.json" ]; then
  echo "Previous genesis block(${script_dir}/genesis.json) exists. Remove or rename the previous genesis block before creating another one."
  exit 102
fi

mkdir -p "${data_dir}"
cd "${data_dir}"
echo "${accounts[password]}" > "passwd"

# Generate Ethereum accounts
declare -a addrs
declare allocs="{"
for (( i=0; i < ${accounts[count]}; i++ )); do
  addrs[i]=`geth account new --keystore keystore --password passwd`
  addrs[i]=${addrs[i]:10:40}
  # echo ${addrs[i]}
  allocs="${allocs}\n    \"0x${addrs[i]}\" : { \"balance\" : \"${balances[i]}\" },"
done

allocs="${allocs%,}\n  }"
# echo -e ${allocs}

# Generate gensis file
sed "s/\"@allocs@\"/${allocs}/g; s/@coinbase@/0x${addrs[0]}/g" \
  "${script_dir}/genesis.template.json" > genesis.json

# Delete account password file
# rm -f "${data_dir}/passwd"

echo "Initializaing private Ethereum network..."

# https://github.com/ethereum/go-ethereum/wiki/Private-network
# geth --datadir "${data_dir}" --unlock ${addrs[1]},${addrs[2]},${addrs[3]} --password "${data_dir}/passwd" init "${script_dir}/genesis.json"
geth --datadir ./ init genesis.json
