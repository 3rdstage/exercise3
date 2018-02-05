#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/private-network-config.sh"
echo "Configuration for private Ethereum network - networkid: $network_id, port: $port, rpcport: $rpc_port, coinbase: $coin_base, datadir: $data_dir"
echo "password: $passwd"

echo "Starting private Ethereum network with single miner in background."
# https://medium.com/@solangegueiros/https-medium-com-solangegueiros-setting-up-ethereum-private-network-on-windows-a72ec59f2198
# parameter candidates : --lightkdf
# APIs : web3, net, eth, db, ssh, admin, debug, miner, personal, txpool
geth --networkid $network_id --identity paul \
  --port $port --rpc --rpcport $rpc_port --rpcapi web3,net,eth,db,admin,personal \
  --mine --minerthreads 4 --etherbase $coin_base \
  --cache 64 --datadir "$data_dir" > "${script_dir}/private-network.log" 2>&1 &

sleep 5s

# extract addresses to which balances allocated in genesis.json
declare -a addrs=`grep -E '^\s*"\S*" : { "balance" : .*$' genesis.json | awk '{print $1}' | tr -d '"'`

# TODO Review to using '--unlock' option at node bootstrap command (geth ...) instead
for addr in ${addrs}; do
  unlocked=`geth --exec "personal.unlockAccount('$addr', '$passwd', 0)" --verbosity 4 attach http://127.0.0.1:$rpc_port`

  if [ "${unlocked}" == "true" ]; then
    echo "Successfully unlocked the address of '${addr}'"
  else
    echo "Fail to unlock the address of '${addr}'"
  fi
done

declare -r enode=`geth --exec "admin.nodeInfo.enode" --verbosity 1 attach http://127.0.0.1:$rpc_port`
echo "The enode for the started node : ${enode}."

echo "Attacing to the network."
geth attach http://127.0.0.1:$rpc_port
