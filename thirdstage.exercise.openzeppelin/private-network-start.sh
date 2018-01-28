#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/private-network-config.sh"
echo "Configuration for private Ethereum network - networkid: $network_id, port: $port, rpcport: $rpc_port, coinbase: $coin_base, datadir: $data_dir"
echo "password: $passwd"

echo "Starting private Ethereum network with single miner in backgound."
# https://medium.com/@solangegueiros/https-medium-com-solangegueiros-setting-up-ethereum-private-network-on-windows-a72ec59f2198
# parameter candidates : --lightkdf
geth --networkid $network_id --port $port --rpc --rpcport $rpc_port --rpcapi personal,db,eth,net,web3 \
  --mine --minerthreads 1 --etherbase $coin_base \
  --cache 64 --datadir "$data_dir" > "${script_dir}/private-network.log" 2>&1 &

sleep 5s

# extract addresses to which balances allocated in genesis.json
declare -a addrs=`grep -E '^\s*"\S*" : { "balance" : .*$' genesis.json | awk '{print $1}' | tr -d '"'`

for addr in ${addrs}; do
  geth --exec "personal.unlockAccount('$addr', '$passwd', 0)" --verbosity 4 attach http://127.0.0.1:$rpc_port

  if [ "$_" == "true" ]; then
    echo "Successfully unlocked the address of '${addr}'"
  else
    echo "Fail to unlock the address of '${addr}'"
  fi
done

echo "Attacing to the network."
geth attach http://127.0.0.1:$rpc_port
