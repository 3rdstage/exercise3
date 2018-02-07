#!/bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/private-network-config.sh"
# echo "Configuration for private Ethereum network - networkid: ${network_id}, port: ${port}, rpcport: ${rpc_port}, datadir: ${data_dir}, coinbase: ${coin_base}"

if [ -f "${data_dir}/geth/chaindata/CURRENT" ]; then
  echo "The private network seems to be initiated already. Check the directory of '${data_dir}'"
  exit 101
fi

if [ -f "${script_dir}/genesis.json" ]; then
  echo "Previous genesis block(${script_dir}/genesis.json) exists. Remove or rename the previous genesis block before creating another one."
  exit 102
fi

mkdir -p "${data_dir}"
echo "${passwd}" > "${data_dir}/passwd"

declare -a addrs
readonly addrs_size=3
allocs="{"

for i in {1..3}; do
  addrs[i]=`geth account new --datadir "${data_dir}" --password "${data_dir}/passwd"`
  addrs[i]=${addrs[i]:10:40}
  # echo ${addrs[i]}
  allocs="${allocs}\n    \"${addrs[i]}\" : { \"balance\" : \"100000000000000000000\" },"
done

allocs="${allocs%,}\n  }"
# echo -e ${allocs}

sed "s/\"@allocs@\"/${allocs}/g" "${script_dir}/genesis.template.json" | sed "s/@coin_base@/${coin_base}/g" > "${script_dir}/genesis.json"
rm -f "${data_dir}/passwd"

echo "Initializaing private Ethereum network..."

# https://github.com/ethereum/go-ethereum/wiki/Private-network
# geth --datadir "${data_dir}" --unlock ${addrs[1]},${addrs[2]},${addrs[3]} --password "${data_dir}/passwd" init "${script_dir}/genesis.json"
geth --datadir "${data_dir}" init "${script_dir}/genesis.json"
