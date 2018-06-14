#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"
readonly run_dir=$(mkdir -p "${script_dir}/../../../run" && cd "${script_dir}/../../../run" && pwd)

echo "Configuration for standalone private Ethereum network - networkid: ${node[networkid]}, port: ${node[port]}, rpcport: ${node[rpcport]}, datadir: $data_dir"

echo "Starting standalone Ethereum network with single miner in background."
# https://medium.com/@solangegueiros/https-medium-com-solangegueiros-setting-up-ethereum-private-network-on-windows-a72ec59f2198
# parameter candidates : --lightkdf
# APIs : web3, net, eth, db, ssh, admin, debug, miner, personal, txpool
cd "${data_dir}"
geth --datadir ./ \
  --port ${node[port]} \
  --networkid ${node[networkid]} \
  --identity ${node[name]} \
  --rpc --rpcaddr ${node[rpcaddr]} --rpcport ${node[rpcport]} \
  --rpcapi web3,net,eth,db,admin,miner,personal,txpool \
  --rpccorsdomain "https://wallet.ethereum.org" \
  --mine --minerthreads 2 \
  --nousb --nodiscover --maxpeers 5 \
  --verbosity ${node[verbosity]} --cache 1024 \
  --unlock 0 --password passwd > geth.log 2>&1 &

sleep 5s

# Extract addresses to which balances allocated in genesis.json
declare -a addrs=`grep -E '^\s*"\S*" : { "balance" : .*$' genesis.json | awk '{print $1}' | tr -d '"'`

# TODO Review to using '--unlock' option at node bootstrap command (geth ...) instead
for addr in ${addrs}; do
  unlocked=`geth --exec "personal.unlockAccount('$addr', '${accounts[password]}', 0)" --verbosity 4 attach http://127.0.0.1:${node[rpcport]}`

  if [ "${unlocked}" == "true" ]; then
    echo "Successfully unlocked the address of '${addr}'"
  else
    echo "Fail to unlock the address of '${addr}'"
  fi
done

declare -r enode=`geth --exec "admin.nodeInfo.enode" --verbosity 1 attach http://127.0.0.1:${node[rpcport]}`
echo "The enode for the started node : ${enode}."

# Write down a few files such as host, accounts, config for current running context
rm -f "${run_dir}/accounts"
# TODO

rm -f "${run_dir}/host"
# TODO

rm -f "${run_dir}/config"
# TODO


echo "Attacing to the network."
geth attach http://127.0.0.1:${node[rpcport]}
